package cn.duwei.springboot.entity;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sys_user
 * @author
 */
@Data
public class SysUser implements Serializable {
    private Integer id;

    private String loginName;

    private String name;

    private String password;

    private String salt;

    private Date registerDate;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    private Date loginDate;

    /**
     * 创建者
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新者
     */
    private Integer updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 删除标记（0：正常；1：删除）
     */
    private String delFlag;

    private static final long serialVersionUID = 1L;
}