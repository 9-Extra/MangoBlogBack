package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.BlogOperation;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.runtime.BlogServiceImpl;
import cn.mango.mangoblog.utils.TokenUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BlogController {
    @Autowired
    private BlogServiceImpl BlogServiceImpl;

    @GetMapping("/blog")
    public ResultWrapper<Blog> getBlog(@RequestParam(value = "id", required = true) Long id) {
        return BlogServiceImpl.GetBlogById(id);
    }

    //发布blog
    @PostMapping("/post")
    public ResultWrapper<Long> Post(@RequestBody BlogOperation operation, @RequestHeader(value = "authorization", required = false) String token) {
//        Optional<DecodedJWT> decodedJWT = TokenUtils.verify(token);//decodedJWT:校验后的token
//        if (decodedJWT.isEmpty()){
//            return new ResultWrapper<>(2, "token错误", 0L);
//        }
//
//        DecodedJWT decodedJWT1 = decodedJWT.get();
        ResultWrapper<Long> verifyresult = TokenUtils.Verify(token);//获取验证结果
        Long user_id=verifyresult.getData();
        if(user_id==0L)
            return verifyresult;
        switch (operation.getOperation()){
            case BlogOperation.OPERATION_NEW -> {
                ResultWrapper<List<Blog>> result=  BlogServiceImpl.GetBlogByAuthorIdAndStauts(user_id,0);//查询该用户是否有空白blog
                //如果没有，则创建空白blog，status置为0
                if(result.getData()==null){
                    Blog blog=new Blog(user_id,0);
                    BlogServiceImpl.insertBlog(blog);
                    return new ResultWrapper<>(blog.getId());
                }
                else return new ResultWrapper<>(0,"Success",result.getData().get(0).getId());//data为blog_id
            }
//                Blog blog = operation.getBlog();
//
//                if (blog == null){
//                    return new ResultWrapper<>(500, "博客不能为空", 0L);
//                }
//
//                blog.setId(user_id);
//                return BlogServiceImpl.insertBlog(blog);
//                if (BlogServiceImpl.insertBlog(blog)){
//                    return new ResultWrapper<>(blog.getId());
//                } else {
//                    return new ResultWrapper<>(500, "上传博客失败", 0L);
//                }
            case BlogOperation.OPERATION_SAVE -> {
                Blog blog=operation.getBlog();
                if(blog==null){
                    return new ResultWrapper<>(500,"博客不能为空",0L);
                }
                if(BlogServiceImpl.UpDateBlogStatus(blog,1)){
                    return new ResultWrapper<>(blog.getId());
                }
                else return new ResultWrapper<>(500,"保存博客失败",0L);
            }
            case BlogOperation.OPERATION_POST -> {
                Blog blog=operation.getBlog();
                if(blog==null){
                    return new ResultWrapper<>(500,"博客不能为空",0L);
                }
                if(BlogServiceImpl.UpDateBlogStatus(blog,2)){
                    return new ResultWrapper<>(blog.getId());
                }
                else return new ResultWrapper<>(500,"上传博客失败",0L);
            }
            case BlogOperation.OPERATION_MODIFY -> {
                Blog blog = operation.getBlog();
                if (blog == null){
                    return new ResultWrapper<>(500, "博客字段不存在", 0L);
                }
                blog.setAuthor_id(user_id);

                if (BlogServiceImpl.UpDateBlog(blog)){
                    return new ResultWrapper<>(blog.getId());
                } else {
                    return new ResultWrapper<>(500, "修改博客失败", 0L);
                }
            }

            case BlogOperation.OPERATION_DELETE -> {
                Long id= operation.getBlog_id();

                if (BlogServiceImpl.deleteBlog(operation.getBlog_id())){
                    return new ResultWrapper<>(500, "删除博客失败", 0L);
                } else {
                    return new ResultWrapper<>(operation.getBlog_id());
                }
            }

            default -> {
                return new ResultWrapper<>(400, "无效操作", 0L);
            }
        }
    }


}