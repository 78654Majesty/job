package com.xxl.job.admin.config;

import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * XXL-Job-Admin 调度配置
 * <p>
 * Created by wfeng on 2018/6/4.
 */
@Configuration
@AutoConfigureAfter(MybatisConfiguration.class)
public class JobConfig {

    @Value("${quartz.configLocation}")
    private String configLocation;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Autowired DataSource dataSource) throws Exception {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setStartupDelay(2000);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setConfigLocation(new ClassPathResource(configLocation));
        return schedulerFactoryBean;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public XxlJobDynamicScheduler xxlJobDynamicScheduler(SchedulerFactoryBean schedulerFactoryBean) {
        XxlJobDynamicScheduler xxlJobDynamicScheduler = new XxlJobDynamicScheduler();
        xxlJobDynamicScheduler.setScheduler(schedulerFactoryBean.getScheduler());
        xxlJobDynamicScheduler.setAccessToken(accessToken);
        return xxlJobDynamicScheduler;
    }

//    @Override
//    public void setEnvironment(Environment environment) {
//        this.configLocation = environment.getProperty("quartz.configLocation");
//    }
}
