package cn.duw.springboot.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源切面
 */

@Slf4j
@Aspect
@Component
public class DataSourceAspect implements Ordered {


    @Pointcut("@annotation(cn.duw.springboot.datasource.DataSourceSelector)")
    public void dataSourcePointCut() {

    }


    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSourceSelector ds = method.getAnnotation(DataSourceSelector.class);
        if (ds == null) {
            DynamicDataSource.setDataSource(DataSourceEnum.LOCAL.getName());
            log.info("set datasource is " + DataSourceEnum.LOCAL.getName());
        } else {
            DynamicDataSource.setDataSource(ds.name().getName());
            log.info("set datasource is " + ds.name());
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.info("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
