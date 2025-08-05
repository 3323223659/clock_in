package com.ruoyi.clock_in.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Sovereign君
 * @ClassName ClassroomSessionVO
 * @Date 2024/10/1 14:49
 * @Version V1.0
 */
@Data
@NoArgsConstructor //好像没什么作用了
public class ClassroomSessionVO {
    private String courseName;
    private String teacherName;
    private String className;
    private List<String> studentNames;
}
