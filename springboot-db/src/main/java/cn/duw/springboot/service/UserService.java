package cn.duw.springboot.service;

import cn.duw.springboot.datasource.DataSourceEnum;
import cn.duw.springboot.datasource.DataSourceSelector;
import cn.duw.springboot.entity.SysUser;
import cn.duw.springboot.mapper.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private SysUserDao sysUserDao;

    @DataSourceSelector(name = DataSourceEnum.TEST)
    public List<SysUser> getUserList() {

        return sysUserDao.getUserList();
    }
}
