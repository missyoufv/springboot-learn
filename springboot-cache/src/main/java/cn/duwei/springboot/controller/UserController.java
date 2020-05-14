package cn.duwei.springboot.controller;

import cn.duwei.springboot.entity.SysUser;
import cn.duwei.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public Object getUserList() {
        List<SysUser> list = userService.getUserList();
        return list;
    }
}
