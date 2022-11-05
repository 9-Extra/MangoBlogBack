package cn.mango.mangoblog.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.example.function")
@ComponentScan(basePackages = "com.example")
public class AutoConfiguration {
}
