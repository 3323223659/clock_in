package com.ruoyi.clock_in.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生对象 clock_in_user
 *
 * @author lzt
 * @date 2024-09-28
 */
public class ClockInUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 学号或用户名
     */
    @Excel(name = "学号或用户名")
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


    private Long classId;

    /**
     * 逻辑删除（0未删除，1删除）
     */
    //@Excel(name = "逻辑删除", readConverterExp = "0=未删除，1删除")
    private Integer isDelete;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
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
                .append("classId", getClassId())
                .append("isDelete", getIsDelete())
                .toString();
    }
}
