package cn.duwei.springboot.spring.study;

import cn.duwei.springboot.spring.bean.Student;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * AbstractApplicationContext类的 refresh 方法构建整个 Ioc 容器过程
 *
 */
public class SpringStudy {


    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student = (Student) context.getBean("student");
        int beanDefinitionCount = context.getBeanDefinitionCount();
        System.out.println(student);
    }
}
