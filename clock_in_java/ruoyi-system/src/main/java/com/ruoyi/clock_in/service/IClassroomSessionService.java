package com.ruoyi.clock_in.service;

import com.ruoyi.clock_in.domain.ClassStudentLogDTO;
import com.ruoyi.clock_in.domain.CourseInfo;
import com.ruoyi.clock_in.domain.StudentsAttendance;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.vo.ClassroomAttendance;
import com.ruoyi.clock_in.domain.vo.ClassroomSessionVO;
import com.ruoyi.clock_in.domain.vo.ClockInClassroomSessionVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @Author Sovereign君
 * @ClassName IClassroomSessionService
 * @Date 2024/10/1 12:58
 * @Version V1.0
 */
public interface IClassroomSessionService {
    // 通过课程位置和日期+时间获取课程id
    Long findClassIdByLocationAndTime( String courseLocation, LocalDate sessionDate, LocalTime time);
    //这个上面的全面
    ClassroomSessionVO getClassroomSession(Long courseId);

    List<StudentsAttendance> getAttendanceByClassroomSession(String ids);

    Long findCourseSessionIdByLocationAndTime(ClassroomSessionDTO classroomSessionDTO);

    List<Long> findCourseSessionIdsByLocationAndTime(ClassroomSessionDTO classroomSessionDTO);


    public List<ClockInClassroomSessionVo> analyticSession(String data,String username);

    Long findSessionIdByClassAndCourse(ClassStudentLogDTO classStudentLogDTO);
}
