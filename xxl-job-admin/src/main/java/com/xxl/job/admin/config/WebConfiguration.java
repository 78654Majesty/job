package com.xxl.job.admin.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.xxl.job.admin.controller.interceptor.CookieInterceptor;
import com.xxl.job.admin.controller.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;

/**
 * WebMvc 配置
 * Created by wfeng on 2018/6/4.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    // UTF8字符集
    private static final Charset UTF8 = Charset.forName("UTF-8");

    // druid监控用户名
    @Value("${druid.username}")
    private String druidUsername;
    // druid监控密码
    @Value("${druid.password}")
    private String druidPassword;


    /**
     * 注册druid监控Servlet
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean druidStatViewServletBean() {
        ServletRegistrationBean druidStatViewServletBean = new ServletRegistrationBean(
                new StatViewServlet(),
                "/druid/*"
        );
        druidStatViewServletBean.addInitParameter("loginUsername", druidUsername);
        druidStatViewServletBean.addInitParameter("loginPassword", druidPassword);
        return druidStatViewServletBean;
    }

    /**
     * 注册druid监控Filter
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidWebStatFilterBean() {
        FilterRegistrationBean druidWebStatFilterBean = new FilterRegistrationBean();
        druidWebStatFilterBean.setFilter(new WebStatFilter());
        druidWebStatFilterBean.addUrlPatterns("/*");
        druidWebStatFilterBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        druidWebStatFilterBean.addInitParameter("sessionStatEnable", "false");
        return druidWebStatFilterBean;
    }

    /**
     * 直接访问jsp的路由
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error404").setViewName("/static/404.html");
        registry.addViewController("/error500").setViewName("/static/500.html");
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CookieInterceptor())
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/jobinfo/add")
                .addPathPatterns("/**");
        registry.addInterceptor(new PermissionInterceptor())
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/jobinfo/add")
                .addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
