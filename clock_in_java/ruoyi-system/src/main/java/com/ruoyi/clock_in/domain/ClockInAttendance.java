package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * @Author Sovereign君
 * @ClassName ClockInAttendance
 * @Date 2024/9/29 19:15
 * @Version V1.0
 */
public class ClockInAttendance  {
    private Long id;
    private Long classroomSessionId;
    private Long studentId;
    private int isPresent;
    private String ipAddress;
    private String location;
    private int isDelete;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public Long getClassroomSessionId() {
        return classroomSessionId;
    }

    public void setClassroomSessionId(Long classroomSessionId) {
        this.classroomSessionId = classroomSessionId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassRoomSessionId() {
        return classroomSessionId;
    }

    public void setClassRoomSessionId(Long classSessionId) {
        this.classroomSessionId = classSessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public int getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(int isPresent) {
        this.isPresent = isPresent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("classSessionId", getClassRoomSessionId())
                .append("studentId", getStudentId())
                .append("isPresent", getIsPresent())
                .append("ipAddress", getIpAddress())
                .append("location", getLocation())
                .toString();
    }
}
