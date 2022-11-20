package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.BlogOperation;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.mapper.BlogMapper;
import cn.mango.mangoblog.runtime.BlogServiceImpl;
import cn.mango.mangoblog.utils.FileUtils;
import cn.mango.mangoblog.utils.TokenUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class BlogController {
    @Autowired
    private BlogServiceImpl blogService;
    private BlogMapper blogMapper;

    @GetMapping("/private/blog")//作者修改blog时向后端请求blog
    public ResultWrapper<Blog> GetPrivateBlogById(@RequestParam(value = "id", required = true) Long id,@RequestHeader(value="authorization",required = true)String token) {
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        if (verifyResult.getCode() != 0){
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        }

        Long user_id = verifyResult.getData().getId();//以token中的id为最终user_id
        return blogService.GetBlogByIdAndAuthorId(id, user_id);
    }
    @GetMapping("/open/blog")//其他用户查看blog时向后端请求blog
    public ResultWrapper<Blog> GetOpenBlog(@RequestParam(value = "id", required = true) Long id){
        return blogService.GetOpenBlogById(id);
    }

    @GetMapping("/private/blogs")//作者查看自己的所有blog
    public ResultWrapper<List<Blog>> GetPrivateBlogs(@RequestHeader(value = "authorization",required = true)String token){
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Long user_id = verifyresultData.getId();//以token中的id为最终user_id
        return blogService.GetAllBlogsByAuthorId(user_id);
    }

    @GetMapping("/open/blogs")//用户查看某个作者的所有blog
    public ResultWrapper<List<Blog>> GetOpenBlogsByAuthorId (@RequestParam(value = "id",required = true)Long id){
        return new ResultWrapper<>(blogService.GetBlogsByAuthorIdAndStauts(id,1,1));
    }

    private ResultWrapper<Long> change_blog_state(long blog_id, long user_id, Integer status, long privilege){
        if (status == null){
            return new ResultWrapper<>(3, "请求字段取值非法", null);
        }

        //如果是管理员
        if (privilege == 1){
            if (blogService.upDateBlogStatusAdmin(blog_id, status)){
                return new ResultWrapper<>(blog_id);
            } else {
                return new ResultWrapper<>(500, "指定博客可能不存在", null);
            }
        }
        //用户
        if (blogService.upDateBlogStatusUser(blog_id, status, user_id)){
            return new ResultWrapper<>(blog_id);
        }

        return new ResultWrapper<>(500, "指定博客可能不存在，或这该博客作者并不是您", null);
    }

    //发布blog
    @PostMapping("/post")
    public ResultWrapper<Long> Post(@RequestBody BlogOperation operation, @RequestHeader(value = "authorization", required = false) String token) throws IOException {
        //校验token
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);

        long user_id = verifyresultData.getId();//以token中的id为最终user_id
        if (operation.getBlog_id() == null){
            return new ResultWrapper<>(3, "必须指定有效的blog_id", null);
        }

        long blog_id = operation.getBlog_id();

        //开始执行操作
        switch (operation.getOperation()) {
            case BlogOperation.OPERATION_NEW -> {//创建空白blog,如果有则返回
                List<Blog> result = blogService.GetBlogsByAuthorIdAndStauts(user_id, -1,-1);//查询该用户是否有空白blog
                if (result.isEmpty()) {
                    //创建空白blog
                    Blog blog = new Blog(0L, user_id, -1, -1,"默认描述", "# 请在此输入内容");
                    Long id = blogService.insertBlog(blog);
                    if (id == null){
                        return new ResultWrapper<>(500, "创建博客失败，这怎么可能", null);
                    }
                    return new ResultWrapper<>(id);
                } else {
                    return new ResultWrapper<>(0, "Success", result.get(0).getId());//data为blog_id
                }
            }
            case BlogOperation.OPERATION_SAVE-> {//作者决定blog不公开，仅作者
                return change_blog_state(blog_id, user_id, 0,0);
            }
            case BlogOperation.OPERATION_POST -> {//作者决定blog公开,仅作者
                return change_blog_state(blog_id, user_id, 1, 0);
            }
            case BlogOperation.OPERATION_REVOKE -> {//管理员决定blog不公开，仅管理员,如果作者决定公开才能执行此操作
                List<Blog> blogList= blogMapper.selectList(Wrappers.<Blog>lambdaQuery().eq(Blog::getId,blog_id).eq(Blog::getStatusauthor,1));
                return change_blog_state(blog_id, user_id, 0, verifyresultData.getPrivilege());
            }
            case BlogOperation.OPERATION_AGREE -> {
                return change_blog_state(blog_id,user_id,1,verifyresultData.getPrivilege());
            }
            case BlogOperation.OPERATION_MODIFY -> {//用户更新blog，同时将statusadmin设置为-1(审核中)
                String description = operation.getDescription();
                String content = operation.getContent();

                if (description == null || content == null){
                    return new ResultWrapper<>(3, "请求字段取值非法", null);
                }

                if (blogService.UpDateBlog(blog_id, user_id, description, content)) {
                    return new ResultWrapper<>(blog_id);
                } else {
                    return new ResultWrapper<>(500, "指定博客可能不存在，或这该博客作者并不是您", null);
                }
            }

            case BlogOperation.OPERATION_DELETE -> {
                if (!blogService.deleteBlog(blog_id ,user_id)) {
                    return new ResultWrapper<>(500, "删除博客失败", 0L);
                } else {
                    String path = System.getProperty("user.dir") + "/image/upload/" + user_id + blog_id;//blog中图片的文件夹路径
                    FileUtils.Delete(path);//删除文件夹
                    return new ResultWrapper<>(operation.getBlog_id());
                }
            }

            default -> {
                return new ResultWrapper<>(400, "无效操作", 0L);
            }
        }
    }


}