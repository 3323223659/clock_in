package com.ruoyi.clock_in.domain.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @Author Sovereign君
 * @ClassName UserAndClassVO
 * @Date 2024/9/29 16:48
 * @Version V1.0
 */
public class ClockInLogVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @Excel(name = "序号")
    private Long id;

    /**
     * 课堂名称
     */
    @Excel(name = "课堂名称")
    private String sourceName;

    /**
     * 上课时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上课时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date classTime;

    /**
     * 班级名称
     */
    @Excel(name = "班级名称")
    private String className;

    /**
     * 打卡人数
     */
    @Excel(name = "打卡人数")
    private Long clockCount;

    /**
     * 未打卡人数
     */
    @Excel(name = "未打卡人数")
    private Long noClock;

    /**
     * 迟到人数
     */
    @Excel(name = "迟到人数")
    private Long belated;

    /**
     * 请假人数
     */
    @Excel(name = "请假人数")
    private Long leaved;

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

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setClassTime(Date classTime) {
        this.classTime = classTime;
    }

    public Date getClassTime() {
        return classTime;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClockCount(Long clockCount) {
        this.clockCount = clockCount;
    }

    public Long getClockCount() {
        return clockCount;
    }

    public void setNoClock(Long noClock) {
        this.noClock = noClock;
    }

    public Long getNoClock() {
        return noClock;
    }

    public void setBelated(Long belated) {
        this.belated = belated;
    }

    public Long getBelated() {
        return belated;
    }

    public void setLeaved(Long leaved) {
        this.leaved = leaved;
    }

    public Long getLeaved() {
        return leaved;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sourceName", getSourceName())
                .append("classTime", getClassTime())
                .append("className", getClassName())
                .append("clockCount", getClockCount())
                .append("noClock", getNoClock())
                .append("belated", getBelated())
                .append("leaved", getLeaved())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("isDelete", getIsDelete())
                .toString();
    }
}
