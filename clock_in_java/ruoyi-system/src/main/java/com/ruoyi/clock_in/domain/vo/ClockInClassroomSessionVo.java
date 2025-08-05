package com.ruoyi.clock_in.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/10/3 上午1:04
 */
@Data
public class ClockInClassroomSessionVo {

    private Long id;

    /**
     * 课程id
     */
    private Long courseId;

    private String courseName;

    /**
     * 上课地点
     */
    private String courseLocation;


    /**
     * 班级id
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 上课日期
     */
    private String sessionDate;

    /**
     * 上课开始时间
     */
    private String startTime;

    /**
     * 下课时间
     */
    private String endTime;

    /**
     * 打卡状态
     */
    private int attendanceStatus;
}
