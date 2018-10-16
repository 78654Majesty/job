package com.xxl.job.admin.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * mybatis 配置数据源类
 * <p>
 * Created by wfeng on 2018/6/4.
 */
@Configuration
@EnableTransactionManagement
@Slf4j
public class MybatisConfiguration {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Value("${mybatis.xmlLocation}")
    private String xmlLocation;
    private String typeAliasesPackage;

    @Bean
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(StringUtils.isNotBlank(dataSourceProperties.getDriveClassName()) ? dataSourceProperties.getDriveClassName() : "com.mysql.jdbc.Driver");
        druidDataSource.setMaxActive(StringUtils.isNotBlank(dataSourceProperties.getMaxActive()) ? Integer.parseInt(dataSourceProperties.getMaxActive()) : 10);
        druidDataSource.setInitialSize(StringUtils.isNotBlank(dataSourceProperties.getInitialSize()) ? Integer.parseInt(dataSourceProperties.getInitialSize()) : 1);
        druidDataSource.setMaxWait(StringUtils.isNotBlank(dataSourceProperties.getMaxWait()) ? Integer.parseInt(dataSourceProperties.getMaxWait()) : 60000);
        druidDataSource.setMinIdle(StringUtils.isNotBlank(dataSourceProperties.getMinIdle()) ? Integer.parseInt(dataSourceProperties.getMinIdle()) : 3);
        druidDataSource.setTimeBetweenEvictionRunsMillis(StringUtils.isNotBlank(dataSourceProperties.getTimeBetweenEvictionRunsMillis()) ?
                Integer.parseInt(dataSourceProperties.getTimeBetweenEvictionRunsMillis()) : 60000);
        druidDataSource.setMinEvictableIdleTimeMillis(StringUtils.isNotBlank(dataSourceProperties.getMinEvictableIdleTimeMillis()) ?
                Integer.parseInt(dataSourceProperties.getMinEvictableIdleTimeMillis()) : 300000);
        druidDataSource.setValidationQuery(StringUtils.isNotBlank(dataSourceProperties.getValidationQuery()) ? dataSourceProperties.getValidationQuery() : "select 'x'");
        druidDataSource.setTestWhileIdle(StringUtils.isNotBlank(dataSourceProperties.getTestWhileIdle()) ? Boolean.parseBoolean(dataSourceProperties.getTestWhileIdle()) : true);
        druidDataSource.setTestOnBorrow(StringUtils.isNotBlank(dataSourceProperties.getTestOnBorrow()) ? Boolean.parseBoolean(dataSourceProperties.getTestOnBorrow()) : false);
        druidDataSource.setTestOnReturn(StringUtils.isNotBlank(dataSourceProperties.getTestOnReturn()) ? Boolean.parseBoolean(dataSourceProperties.getTestOnReturn()) : false);
        druidDataSource.setPoolPreparedStatements(StringUtils.isNotBlank(dataSourceProperties.getPoolPreparedStatements()) ? Boolean.parseBoolean(dataSourceProperties.getPoolPreparedStatements()) : true);
        druidDataSource.setMaxOpenPreparedStatements(StringUtils.isNotBlank(dataSourceProperties.getMaxOpenPreparedStatements()) ? Integer.parseInt(dataSourceProperties.getMaxOpenPreparedStatements()) : 20);

        try {
            druidDataSource.setFilters(StringUtils.isNotBlank(dataSourceProperties.getFilters()) ? dataSourceProperties.getFilters() : "stat, wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
        log.info("url={}", dataSourceProperties.getUrl());
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        if (StringUtils.isNotBlank(typeAliasesPackage)) {
            bean.setTypeAliasesPackage(typeAliasesPackage);
        }
        //分页插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Interceptor[] plugins = new Interceptor[]{pageInterceptor};
        bean.setPlugins(plugins);
        try {
            bean.setMapperLocations(resolver.getResources(xmlLocation));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Override
//    public void setEnvironment(Environment environment) {
//        this.url = environment.getProperty("spring.datasource.url");
//        logger.info("url={}", this.url);
//        this.username = environment.getProperty("spring.datasource.username");
//        this.password = environment.getProperty("spring.datasource.password");
//        this.driveClassName = environment.getProperty("spring.datasource.driver-class-name");
//        this.filters = environment.getProperty("spring.datasource.filters");
//        this.maxActive = environment.getProperty("spring.datasource.maxActive");
//        this.initialSize = environment.getProperty("spring.datasource.initialSize");
//        this.maxWait = environment.getProperty("spring.datasource.maxWait");
//        this.minIdle = environment.getProperty("spring.datasource.minIdle");
//        this.timeBetweenEvictionRunsMillis = environment.getProperty("spring.datasource.timeBetweenEvictionRunsMillis");
//        this.minEvictableIdleTimeMillis = environment.getProperty("spring.datasource.minEvictableIdleTimeMillis");
//        this.validationQuery = environment.getProperty("spring.datasource.validationQuery");
//        this.testWhileIdle = environment.getProperty("spring.datasource.testWhileIdle");
//        this.testOnBorrow = environment.getProperty("spring.datasource.testOnBorrow");
//        this.testOnReturn = environment.getProperty("spring.datasource.testOnReturn");
//        this.poolPreparedStatements = environment.getProperty("spring.datasource.poolPreparedStatements");
//        this.maxOpenPreparedStatements = environment.getProperty("spring.datasource.maxOpenPreparedStatements");
//        this.typeAliasesPackage = environment.getProperty("mybatis.typeAliasesPackage");
//        this.xmlLocation = environment.getProperty("mybatis.xmlLocation");
//    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
