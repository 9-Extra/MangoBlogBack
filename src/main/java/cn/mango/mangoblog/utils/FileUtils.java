package cn.mango.mangoblog.utils;

import cn.mango.mangoblog.entity.ResultWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;
@Primary
@Service
public class FileUtils {
    public static String Upload(MultipartFile file, Long id) throws IOException {
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
        return "/upload" + id + "/" + filename;
    }

    //下载时获取输入流
    private static InputStream getImgInputStream(String imgPath) throws FileNotFoundException {
        return new FileInputStream(imgPath);
    }

    //下载图片
    public static void Download(HttpServletResponse resp, String url) throws IOException {
        String path = System.getProperty("user.dir") + "/image" + url;
        final InputStream in = getImgInputStream(path);
        resp.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, resp.getOutputStream());
    }

    public static void Delete(String spath) throws IOException {//删除图片及文件夹，spath指明文件时删除文件，否则删除文件夹,spath为文件真实路径
        Path path = Paths.get(spath);
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                return super.preVisitDirectory(dir, attrs);
//            }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.printf("Delete file : %s%n", file);
                        return FileVisitResult.CONTINUE;
                    }

//            @Override
//            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
//                return super.visitFileFailed(file, exc);
//            }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc)  {
                        try {
                            Files.delete(dir);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.printf("Delete folder : %s%n", dir);
                        return FileVisitResult.CONTINUE;
                    }
                }
        );
    }
}
