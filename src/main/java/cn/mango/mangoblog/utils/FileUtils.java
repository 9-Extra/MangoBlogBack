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
    public static String Upload(MultipartFile file, Long blog_id, Long user_id) {//放入/image/upload/user_id/blog_id下
        //上传路径保存设置
        //获得SpringBoot当前项目的路径：System.getProperty("user.dir")
        String system_root = System.getProperty("user.dir") + "/image";
        String path;
        if (blog_id != null) {
            path = String.format("/upload/%d/%d/%s", user_id, blog_id, file.getOriginalFilename());
        } else {
            path = String.format("/upload/%d/%s", user_id, file.getOriginalFilename());
        }
        File file_save = new File(system_root, path);
        //路径不存在就创建路径
        file_save.mkdirs();

        //解决文件名字问题：我们使用uuid;
        //String filename = "pg-" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";//后缀

        //存入新文件
        try {
            file.transferTo(file_save);
            //上传文件地址
            System.out.println("上传文件保存地址：" + file_save.getAbsolutePath());
        } catch (IOException e) {
            return null;
        }
        System.out.println("============上传成功============");
        return path;
    }

    public static void Delete(String spath) {//删除图片及文件夹，spath指明文件时删除文件，否则删除文件夹,spath为文件真实路径
        Path path = Paths.get(spath);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.printf("Delete file : %s%n", file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                            try {
                                Files.delete(dir);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.printf("Delete folder : %s%n", dir);
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ModifyProfile(MultipartFile file, Long user_id) {
        String path = System.getProperty("user.dir") + "/image/upload/" + user_id;
        Delete(path);
        if (file != null) {
            Upload(file, null, user_id);
            return "Modify profile success";
        } else return "Delete profile success";
    }
}
