package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.Blog;
import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.mapper.BlogMapper;
import cn.mango.mangoblog.utils.FileUtils;
import cn.mango.mangoblog.utils.TokenUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@RestController
public class FileController {
    public FileController(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    //获取输入流
//    private InputStream getImgInputStream(String imgPath) throws FileNotFoundException {
//        return new FileInputStream(new File(imgPath));
//    }
    private BlogMapper blogMapper;

    //blog中图片上传
    @PostMapping(value = "/file/upload", produces = "application/json")//上传blog中图片
    public ResultWrapper<String> imageUpload(@RequestBody MultipartFile file, @RequestHeader(value = "authorization",required = true)String token,@RequestParam(value = "id",required = true)Long blog_id) throws IOException {
        ResultWrapper<VerifyResult> verifyresult = TokenUtils.Verify(token);
        if(verifyresult.getData()==null)
            return new ResultWrapper<>(verifyresult.getCode(), verifyresult.getMessage(), "");
        Long user_id=verifyresult.getData().getId();
        List<Blog> blogList= blogMapper.selectList(Wrappers.<Blog>lambdaQuery().eq(Blog::getId,blog_id).eq(Blog::getAuthorid,user_id));
        if(blogList.isEmpty())
            return new ResultWrapper<>(400,"Certain blog not found",null);
        String result = FileUtils.Upload(file, blog_id,verifyresult.getData().getId());
        return new ResultWrapper<>(result);
    }
    @PostMapping(value = "/profile",produces = "application/json")//上传及修改头像
    public ResultWrapper<String> profileUpload(@RequestBody(required = false)MultipartFile file, @RequestHeader(value = "authorization",required = true)String token){
        ResultWrapper<VerifyResult> verifyresult = TokenUtils.Verify(token);
        if(verifyresult.getData()==null)
            return new ResultWrapper<>(verifyresult.getCode(), verifyresult.getMessage(), "");
        Long user_id=verifyresult.getData().getId();
        String result=FileUtils.ModifyProfile(file,user_id);
        return new ResultWrapper<>(0,"Success",result);
    }
    //图片下载
    @PostMapping("/image/download")//下载blog中图片
    public ResultWrapper<OutputStream> imageDownload(HttpServletResponse resp, @RequestParam(value = "url", required = false) String url,@RequestParam(value = "id",required = false)Long id)  {
        resp.setContentType(MediaType.ALL_VALUE);//IMAGE_PNG_VALUE
        try {
            return FileUtils.Download(resp.getOutputStream(), url,id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultWrapper<>(800,"HttpServletResponse.getOutputStream failed",null);
        }
    }

//    @PostMapping("/image/delete")//删除单个文件
//    public ResultWrapper<Long> deleteFile(@RequestParam(value = "url", required = true) String url) throws IOException {
//        String path = System.getProperty("user.dir") + "/image" + url;//文件真实路径
//        FileServiceImpl.Delete(path);
//        return new ResultWrapper<>(0, "Success", -1L);
//    }


}
