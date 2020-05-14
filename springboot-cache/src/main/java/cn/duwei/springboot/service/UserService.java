package cn.duwei.springboot.service;

import cn.duwei.springboot.dao.UserMapper;
import cn.duwei.springboot.entity.SysUser;
import cn.duwei.springboot.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.duwei.springboot.constant.CacheConstant.USER_LIST_REDIS_PREX;

@Service
@Slf4j
public class UserService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

//    @Cacheable(value = "cachetest")
    public List<SysUser> getUserList() {
        List<SysUser> userList = null;
        String value = stringRedisTemplate.opsForValue().get(USER_LIST_REDIS_PREX);
        if (value == null) {
            log.info("get data from db");
            userList = userMapper.getUserList();
            stringRedisTemplate.opsForValue().set(USER_LIST_REDIS_PREX, JsonUtil.toJson(userList));
        } else {
            log.info("get data from cache");
            userList = JsonUtil.toBean(value, new TypeReference<List<SysUser>>() {});
        }
        return userList;
    }
}
