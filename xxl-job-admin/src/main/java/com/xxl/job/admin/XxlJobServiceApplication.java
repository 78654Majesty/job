package com.xxl.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.xxl.job.admin.mapper")
public class XxlJobServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxlJobServiceApplication.class, args);
    }

}
