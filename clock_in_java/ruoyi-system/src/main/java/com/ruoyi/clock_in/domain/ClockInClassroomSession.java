package com.ruoyi.clock_in.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @Author Sovereign君
 * @ClassName ClockInClassroomSession
 * @Date 2024/10/1 11:53
 * @Version V1.0
 */
@Data
@NoArgsConstructor
public class ClockInClassroomSession extends BaseEntity {
    /**
     * 主键
     */
    private Long id;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 上课地点
     */
    private String courseLocation;

    /**
     * 班级id
     */
    private Long classId;

    /**
     * 上课日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sessionDate;

    /**
     * 上课开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    /**
     * 下课时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    private Integer isDelete;
}
