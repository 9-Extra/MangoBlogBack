package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.ResultWrapper;

import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.mapper.BlogMapper;

import cn.mango.mangoblog.runtime.UserServiceImpl;
import cn.mango.mangoblog.utils.FileUtils;
import cn.mango.mangoblog.utils.TokenUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Objects;


@RestController
public class FileController {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserServiceImpl userService;

    //blog中图片上传
    @PostMapping(value = "/file/upload", produces = "application/json")//上传blog中图片
    public ResultWrapper<String> imageUpload(@RequestBody MultipartFile file, @RequestHeader(value = "authorization", required = true) String token, @RequestParam(value = "id", required = true) Long blog_id)  {
        ResultWrapper<VerifyResult> verifyresult = TokenUtils.Verify(token);
        if (verifyresult.getData() == null)
            return new ResultWrapper<>(verifyresult.getCode(), verifyresult.getMessage(), "");
        Long user_id = verifyresult.getData().getId();
        List<Blog> blogList = blogMapper.selectList(Wrappers.<Blog>lambdaQuery().eq(Blog::getId, blog_id).eq(Blog::getAuthorid, user_id));
        if (blogList.isEmpty())
            return new ResultWrapper<>(400, "Certain blog not found", null);
        String result = FileUtils.Upload(file, blog_id, verifyresult.getData().getId());
        return new ResultWrapper<>(result);
    }

    @PostMapping(value = "/profile", produces = "application/json")//上传及修改头像
    public ResultWrapper<Void> profileUpload(@RequestBody(required = false) MultipartFile file, @RequestHeader(value = "authorization", required = true) String token) {
        ResultWrapper<VerifyResult> verifyresult = TokenUtils.Verify(token);
        if (verifyresult.getCode() != 0) {
            return new ResultWrapper<>(verifyresult.getCode(), verifyresult.getMessage(), null);
        }
        long user_id = verifyresult.getData().getId();

        if (file == null || file.isEmpty()){
            //仅仅删除头像
            FileUtils.delete_file_by_url(userService.get_head_image_url(user_id));
            userService.set_head_image_url(user_id, null);
            return new ResultWrapper<>(null);
        } else {
            String result = FileUtils.Upload(file, null, user_id);
            if (result == null){
                //上传失败了
                return new ResultWrapper<>(500, "上传失败", null);
            } else {
                //需要删除原来的头像，并设置新的url
                String url = userService.get_head_image_url(user_id);
                //头像和原来的路径相同时不要删除
                if (!Objects.equals(url, result)){
                    FileUtils.delete_file_by_url(url);
                    userService.set_head_image_url(user_id, result);
                }
                return new ResultWrapper<>(null);
            }
        }
    }
}
