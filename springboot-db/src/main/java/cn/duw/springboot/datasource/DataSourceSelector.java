package cn.duw.springboot.datasource;

import java.lang.annotation.*;

/**
 * 指定要使用的数据源
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceSelector {

    DataSourceEnum name() default DataSourceEnum.LOCAL;
}
