package cn.duw.springboot.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {


    @Bean
    @ConfigurationProperties("spring.datasource.druid.test")
    public DataSource testDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.local")
    public DataSource localDataSource(){

        return DruidDataSourceBuilder.create().build();
    }


    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource localDataSource, DataSource testDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(5);
        targetDataSources.put(DataSourceEnum.LOCAL.getName(), localDataSource);
        targetDataSources.put(DataSourceEnum.TEST.getName(), testDataSource);
        return new DynamicDataSource(localDataSource, targetDataSources);
    }
}
