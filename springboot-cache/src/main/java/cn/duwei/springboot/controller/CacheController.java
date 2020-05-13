package cn.duwei.springboot.controller;

import cn.duwei.springboot.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/cache")
@RestController
public class CacheController {

    @Autowired
    private CacheService cacheService;


    @GetMapping("/get")
    public String getCache(String key) {

        cacheService.getCache(key);
        return "SUCCESS";
    }

    @GetMapping("/put")
    public String putCache(String key) {

        cacheService.put(key);
        return "SUCCESS";
    }

    @GetMapping("/delete")
    public String deleteCache(String key) {

        cacheService.delete(key);
        return "SUCCESS";
    }
}
