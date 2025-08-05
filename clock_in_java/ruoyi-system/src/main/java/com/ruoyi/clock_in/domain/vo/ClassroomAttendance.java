package com.ruoyi.clock_in.domain.vo;

import com.ruoyi.clock_in.domain.StudentsAttendance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @Author Sovereign君
 * @ClassName ClassroomAttendance
 * @Date 2024/10/1 18:26
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomAttendance {
    private String courseName;
    private String teacherName;
    private String className;
    private List<StudentsAttendance> studentsAttendance;
}
