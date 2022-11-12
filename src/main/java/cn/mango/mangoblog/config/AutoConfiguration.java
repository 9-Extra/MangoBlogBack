package cn.mango.mangoblog.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "cn.mango.mangoblog.function")
@ComponentScan(basePackages = "cn.mango.mangoblog")
public class AutoConfiguration {
}
