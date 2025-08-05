package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;


/**
 * @Author Sovereign君
 * @ClassName ClassroomAndCourseData
 * @Date 2024/10/1 15:20
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomAndCourseData extends BaseEntity {
    private String courseName;
    private String teacherName;
    private String className;
    private String studentNames;
}
