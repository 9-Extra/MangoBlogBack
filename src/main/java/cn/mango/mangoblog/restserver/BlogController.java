package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.BlogOperation;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.runtime.BlogServiceImpl;
import cn.mango.mangoblog.utils.FileUtils;
import cn.mango.mangoblog.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
public class BlogController {
    @Autowired
    private BlogServiceImpl BlogServiceImpl;

    @GetMapping("/private/blog")//作者修改blog时向后端请求blog
    public ResultWrapper<Blog> GetPrivateBlogById(@RequestParam(value = "id", required = true) Long id,@RequestHeader(value="authorization",required = true)String token) {
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Long user_id = verifyresultData.getId();//以token中的id为最终user_id
        ResultWrapper<Blog> resultWrapper= BlogServiceImpl.GetBlogByIdAndAuthorId(id,user_id);
        if(resultWrapper.getData()==null)
            return new ResultWrapper<>(resultWrapper.getCode(), resultWrapper.getMessage(), null);
        return BlogServiceImpl.GetBlogById(id);
    }
    @GetMapping("/open/blog")//其他用户查看blog时向后端请求blog
    public ResultWrapper<Blog> GetOpenBlog(@RequestParam(value = "id", required = true) Long id){
        return BlogServiceImpl.GetOpenBlogById(id);
    }

    @GetMapping("/private/blogs")//作者查看自己的所有blog
    public ResultWrapper<List<Blog>> GetPrivateBlogs(@RequestHeader(value = "authorization",required = true)String token){
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Long user_id = verifyresultData.getId();//以token中的id为最终user_id
        return BlogServiceImpl.GetAllBlogsByAuthorId(user_id);
    }

    @GetMapping("/open/blogs")//用户查看某个作者的所有blog
    public ResultWrapper<List<Blog>> GetOpenBlogsByAuthorId (@RequestParam(value = "id",required = true)Long id){
        return BlogServiceImpl.GetBlogsByAuthorIdAndStauts(id,2);
    }



    //发布blog
    @PostMapping("/post")
    public ResultWrapper<Long> Post(@RequestBody BlogOperation operation, @RequestHeader(value = "authorization", required = false) String token) throws IOException {
//        Optional<DecodedJWT> decodedJWT = TokenUtils.verify(token);//decodedJWT:校验后的token
//        if (decodedJWT.isEmpty()){
//            return new ResultWrapper<>(2, "token错误", 0L);
//        }
//
//        DecodedJWT decodedJWT1 = decodedJWT.get();
        ResultWrapper<VerifyResult> verifyResult = TokenUtils.Verify(token);//获取验证结果
        VerifyResult verifyresultData = verifyResult.getData();
        if (verifyresultData == null)
            return new ResultWrapper<>(verifyResult.getCode(), verifyResult.getMessage(), null);
        Long user_id = verifyresultData.getId();//以token中的id为最终user_id
        switch (operation.getOperation()) {
            case BlogOperation.OPERATION_NEW -> {
                ResultWrapper<List<Blog>> result = BlogServiceImpl.GetBlogsByAuthorIdAndStauts(user_id, 0);//查询该用户是否有空白blog
                //如果没有，则创建空白blog，status置为0
                if (result.getData() == null) {
                    Blog blog = new Blog(user_id, 0);
                    return BlogServiceImpl.insertBlog(blog);
                } else return new ResultWrapper<>(0, "Success", result.getData().get(0).getId());//data为blog_id
            }
            case BlogOperation.OPERATION_SAVE-> {
                return BlogServiceImpl.UpdateBlogStatusByBlogOperation(operation, 1,user_id);
            }
            case BlogOperation.OPERATION_POST -> {
//                Blog blog=operation.getBlog();
//                if(blog==null){
//                    return new ResultWrapper<>(500,"博客不能为空",0L);
//                }
//                if(BlogServiceImpl.UpDateBlogStatus(blog,2)){
//                    return new ResultWrapper<>(blog.getId());
//                }
//                else return new ResultWrapper<>(500,"上传博客失败",0L);
                return BlogServiceImpl.UpdateBlogStatusByBlogOperation(operation, 2,user_id);
            }
            case BlogOperation.OPERATION_REVOKE -> {
                if(verifyresultData.getPrivilege()!=1|| !Objects.equals(user_id, operation.getBlog_id())){
                    return new ResultWrapper<>(2,"Invalid token",0L);
                }
                return BlogServiceImpl.UpdateBlogStatusByBlogOperation(operation,1, user_id);
            }
            case BlogOperation.OPERATION_MODIFY -> {
                Blog blog = operation.getBlog();
                if (blog == null) {
                    return new ResultWrapper<>(500, "博客字段不存在", 0L);
                }
                blog.setAuthorid(user_id);

                if (BlogServiceImpl.UpDateBlog(blog)) {
                    return new ResultWrapper<>(blog.getId());
                } else {
                    return new ResultWrapper<>(500, "修改博客失败", 0L);
                }
            }

            case BlogOperation.OPERATION_DELETE -> {
                Long id = operation.getBlog_id();
//                Boolean delete = BlogServiceImpl.deleteBlog(operation.getBlog_id(),user_id);
                if (!BlogServiceImpl.deleteBlog(operation.getBlog_id(),user_id)) {
                    return new ResultWrapper<>(500, "删除博客失败", 0L);
                } else {
                    String path = System.getProperty("user.dir") + "/image/upload/" + id;//blog中图片的文件夹路径
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