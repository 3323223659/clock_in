package com.ruoyi.clock_in.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @Author Sovereign君
 * @ClassName UserAndClassVO
 * @Date 2024/9/29 15:48
 * @Version V1.0
 */
public class UserAndClassVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 学号或用户名
     */
    @Excel(name = "学号")
    private String username;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String name;



    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;
    /**
     * 班级
     */
    @Excel(name = "班级")
    private String className;

    /**
     * 逻辑删除（0未删除，1删除）
     */
    //@Excel(name = "逻辑删除", readConverterExp = "0=未删除，1删除")
    private Integer isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("username", getUsername())
                .append("name", getName())
                .append("password", getPassword())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("className", getClassName())
                .append("isDelete", getIsDelete())
                .toString();
    }
}
