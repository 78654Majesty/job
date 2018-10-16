//package com.xxl.job.admin.config;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import tk.mybatis.spring.mapper.MapperScannerConfigurer;
//
///**
// * mybatis mapper 扫描配置类
// * <p>
// * Created by wfeng on 2018/6/4.
// */
//@Configuration
//@AutoConfigureAfter(MybatisConfiguration.class)
//public class MapperConfiguration {
//
//    private Environment evn;
//
//    @Value("${mybatis.basepackage}")
//    private String basePackage;
//
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(Environment environment) {
//
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        mapperScannerConfigurer.setBasePackage(basePackage);
//        return mapperScannerConfigurer;
//    }
//
////    @Override
////    public void setEnvironment(Environment environment) {
////        this.basePackage = evn.getProperty("mybatis.basepackage");
////    }
//
//
//}