package cn.mango.mangoblog.restserver;

import cn.mango.mangoblog.entity.ResultWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
public class FileController {
    //获取输入流
    private InputStream getImgInputStream(String imgPath) throws FileNotFoundException {
        return new FileInputStream(new File(imgPath));
    }

    //图片上传
    @PostMapping(value = "/file/upload", produces = "application/json")//指定消息主体的编码方式
    public ResultWrapper<String> fileUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file ,@RequestParam(value="id",required=true)Long id) throws IOException {
        //上传路径保存设置
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        String path = System.getProperty("user.dir") + "/image/upload/";
        //按照月份进行分类：
//        Calendar instance = Calendar.getInstance();
//        String month = (instance.get(Calendar.MONTH) + 1) + "-month";
        path = path + id;//路径(+month)
        File realPath = new File(path);
        //路径不存在就创建路径
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        //上传文件地址
        System.out.println("上传文件保存地址：" + realPath);
        //解决文件名字问题：我们使用uuid;
        String filename = "pg-" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";//后缀
        //生成新文件
        File newfile = new File(realPath, filename);
        //存入新文件
        file.transferTo(newfile);
        System.out.println("============上传成功");
        String url = "/upload" + id + "/" + filename;
        return new ResultWrapper<>(0, "Success", url);
        //给editormd进行回调
    }

    //图片下载
    @PostMapping("/image/download")
    public void getImage(HttpServletResponse resp, @RequestParam(value = "url", required = true) String url) throws IOException {
        String path = System.getProperty("user.dir") + "/image" + url;
        final InputStream in = getImgInputStream(path);
        resp.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, resp.getOutputStream());
    }
//    public void deleteFile(String url)throws IOException{
//        String url0 = System.getProperty("user.dir") + "/image" + url;
//        Path path= Paths.get(url0);
//        Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                return super.preVisitDirectory(dir, attrs);
//            }
//
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                return super.visitFile(file, attrs);
//            }
//
//            @Override
//            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//                return super.visitFileFailed(file, exc);
//            }
//
//            @Override
//            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                return super.postVisitDirectory(dir, exc);
//            }
//        })
//    }
    //    @Test
//    void testDeleteFileDir5() throws IOException {
//        createMoreFiles();
//        Path path = Paths.get("D:\data\test1\test2");
//
//        Files.walkFileTree(path,
//                new SimpleFileVisitor<Path>() {
//                    // 先去遍历删除文件
//                    @Override
//                    public FileVisitResult visitFile(Path file,
//                                                     BasicFileAttributes attrs) throws IOException {
//                        Files.delete(file);
//                        System.out.printf("文件被删除 : %s%n", file);
//                        return FileVisitResult.CONTINUE;
//                    }
//                    // 再去遍历删除目录
//                    @Override
//                    public FileVisitResult postVisitDirectory(Path dir,
//                                                              IOException exc) throws IOException {
//                        Files.delete(dir);
//                        System.out.printf("文件夹被删除: %s%n", dir);
//                        return FileVisitResult.CONTINUE;
//                    }
//
//                }
//        );
//
//    }


}
