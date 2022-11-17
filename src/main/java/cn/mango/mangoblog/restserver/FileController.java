package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.VerifyResult;
import cn.mango.mangoblog.utils.FileUtils;
import cn.mango.mangoblog.utils.TokenUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


@RestController
public class FileController {
    //获取输入流
    private InputStream getImgInputStream(String imgPath) throws FileNotFoundException {
        return new FileInputStream(new File(imgPath));
    }

    //图片上传
    @PostMapping(value = "/file/upload", produces = "application/json")//指定消息主体的编码方式
    public ResultWrapper<String> imageUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, @RequestHeader(value = "authorization",required = true)String token) throws IOException {
        ResultWrapper<VerifyResult> verifyresult = TokenUtils.Verify(token);
        if(verifyresult.getData()==null)
            return new ResultWrapper<>(verifyresult.getCode(), verifyresult.getMessage(), "");
        String result = FileUtils.Upload(file, verifyresult.getCode());
        return new ResultWrapper<>(result);
    }

    //图片下载
    @PostMapping("/image/download")
    public void imageDownload(HttpServletResponse resp, @RequestParam(value = "url", required = true) String url) throws IOException {
        FileUtils.Download(resp, url);
    }

//    @PostMapping("/image/delete")//删除单个文件
//    public ResultWrapper<Long> deleteFile(@RequestParam(value = "url", required = true) String url) throws IOException {
//        String path = System.getProperty("user.dir") + "/image" + url;//文件真实路径
//        FileServiceImpl.Delete(path);
//        return new ResultWrapper<>(0, "Success", -1L);
//    }


}
