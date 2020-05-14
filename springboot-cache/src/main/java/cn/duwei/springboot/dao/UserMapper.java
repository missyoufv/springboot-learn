package cn.duwei.springboot.dao;

import cn.duwei.springboot.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<SysUser> getUserList();
}
