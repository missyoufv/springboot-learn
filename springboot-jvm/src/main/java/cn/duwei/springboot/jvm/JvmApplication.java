package cn.duwei.springboot.jvm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * jvm项目启动类
 */
@SpringBootApplication
@Slf4j
public class JvmApplication {

    public static void main(String[] args) {
        log.info(" ================== JvmApplication success start ==================");
        SpringApplication.run(JvmApplication.class);
    }
}
