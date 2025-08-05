package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @Author Sovereign君
 * @ClassName ClockInClass
 * @Date 2024/9/29 23:11
 * @Version V1.0
 */
public class ClockInClass extends BaseEntity {
    private Long id;
    private String className;
    private Integer isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
