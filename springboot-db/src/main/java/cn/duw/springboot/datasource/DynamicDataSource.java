package cn.duw.springboot.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源
 *
 * determineCurrentLookupKey() 方法决定使用哪个数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * ThreadLocal 线程局部变量，在多线程环境可以保证各个线程里的变量独立于其它线程里的变量。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER  = new ThreadLocal<>();


    /**
     * 决定使用哪个数据源之前需要把多个数据源的信息以及默认数据源信息配置好
     *
     * @param defaultTargetDataSource 默认数据源
     * @param targetDataSources       目标数据源
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        CONTEXT_HOLDER.set(dataSource);
    }

    public static String getDataSource() {
        return CONTEXT_HOLDER.get();
    }


    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}
