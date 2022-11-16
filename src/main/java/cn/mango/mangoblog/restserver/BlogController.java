package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.BlogOperation;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.runtime.BlogServiceImpl;
import cn.mango.mangoblog.utils.TokenUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BlogController {
    @Autowired
    private BlogServiceImpl BlogServiceImpl;

    @GetMapping("/blog")
    public ResultWrapper<Blog> getBlog(@RequestParam(value = "id", required = true) Long id) {
        return BlogServiceImpl.getBlog(id);
    }

    //发布blog
    @PostMapping("/post")
    public ResultWrapper<Long> Post(@RequestBody BlogOperation operation, @RequestHeader(value = "authorization", required = false) String token) {
        Optional<DecodedJWT> decodedJWT = TokenUtils.verify(token);//decodedJWT:校验后的token
        if (decodedJWT.isEmpty()){
            return new ResultWrapper<>(2, "token错误", 0L);
        }

        DecodedJWT decodedJWT1 = decodedJWT.get();
        Long user_id = decodedJWT1.getClaim("user_id").asLong();//获取用户id

        switch (operation.getOperation()){
            case BlogOperation.OPERATION_NEW -> {
                Blog blog = operation.getBlog();

                if (blog == null){
                    return new ResultWrapper<>(500, "博客不能为空", 0L);
                }

                blog.setId(user_id);

                if (BlogServiceImpl.insertBlog(blog)){
                    return new ResultWrapper<>(blog.getId());
                } else {
                    return new ResultWrapper<>(500, "上传博客失败", 0L);
                }
            }
            case BlogOperation.OPERATION_MODIFY -> {
                Blog blog = operation.getBlog();
                if (blog != null && BlogServiceImpl.updateBlog(blog)){
                    return new ResultWrapper<>(blog.getId());
                } else {
                    return new ResultWrapper<>(500, "修改博客失败", 0L);
                }
            }

            case BlogOperation.OPERATION_DELETE -> {
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