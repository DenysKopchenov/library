package com.dkop.library.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
@ComponentScan("com.dkop.library")
public class ApplicationConfig {

    @Bean
    public DataSource dataSource(){
        BasicDataSource basicDataSource = new BasicDataSource();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("db");

        basicDataSource.setUrl(resourceBundle.getString("db.url"));
        basicDataSource.setUsername(resourceBundle.getString("db.username"));
        basicDataSource.setPassword(resourceBundle.getString("db.password"));
        basicDataSource.setDriverClassName(resourceBundle.getString("db.driver"));
        basicDataSource.setMinIdle(Integer.parseInt(resourceBundle.getString("db.min.idle")));
        basicDataSource.setMaxIdle(Integer.parseInt(resourceBundle.getString("db.max.idle")));
        basicDataSource.setMaxOpenPreparedStatements(Integer.parseInt(resourceBundle.getString("db.max.opened.statements")));

        return basicDataSource;
    }
}
