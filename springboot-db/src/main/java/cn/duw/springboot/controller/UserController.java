package cn.duw.springboot.controller;


import cn.duw.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/list")
    public Object getUserList() {
        return userService.getUserList();
    }
}
