package cn.duwei.springboot.spring.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable {

    private Long id;

    private String name;

    private Integer age;

    private String address;

    private Float score;

    Student() {
        id = 12l;
        name = "duw";
        age = 25;
        address = "湖北武汉";
        score = 85.9f;
    }
}
