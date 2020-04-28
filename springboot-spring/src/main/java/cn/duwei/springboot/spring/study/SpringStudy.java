package cn.duwei.springboot.spring.study;

import cn.duwei.springboot.spring.bean.Student;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring源码
 *  1、super(parent);
 *  2、setConfigLocations
 *  3、refresh 核心方法
 *      1、prepareRefresh();
 *      2、ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory(); 创建DefaultListableBeanFactory容器，并解析配置文件，
 *          加载bean信息，封装到beanDefination中，然后维护到map中
 *      3、 prepareBeanFactory(beanFactory);
 *          //BeanFactory的预准备工作（BeanFactory进行一些设置，比如context的类加载器，BeanPostProcessor和XXXAware自动装配等）
 *      4、postProcessBeanFactory(beanFactory);
 *          //BeanFactory准备工作完成后进行的后置处理工作
 *
 */
public class SpringStudy {


    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student = (Student) context.getBean("student");
        System.out.println(student);
    }
}
