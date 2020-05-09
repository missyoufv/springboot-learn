package cn.duw.springboot;

import cn.duw.springboot.datasource.DynamicDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 包含功能点梳理：
 *      1、多数据源动态切换及原理了解
 */
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DBApplication {

    public static void main(String[] args) {

        SpringApplication.run(DBApplication.class);
    }
}
