package com.ruoyi.clock_in.mapper;

import com.ruoyi.clock_in.domain.ClassroomAndCourseData;
import com.ruoyi.clock_in.domain.CourseInfo;
import com.ruoyi.clock_in.domain.ClockInClassroomSession;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @Author Sovereign君
 * @ClassName ClockInClassroomSessionMapper
 * @Date 2024/10/1 12:51
 * @Version V1.0
 */
public interface ClockInClassroomSessionMapper {
    /**
     *根据课堂位置，开始时间信息查询班级id，没有用到
     */
    Long findClassIdByLocationAndTime(@Param("courseLocation") String courseLocation,
                                      @Param("sessionDate") LocalDate sessionDate,
                                      @Param("time")LocalTime time);

    /**
     * 根据课堂id查询班级信息等，只能得到一个班级的，没有用到
     */
    ClassroomAndCourseData getClassroomSession(@Param("courseId") Long courseId);

    /**
     * 根据课堂id集合查询所有班级信息等
     */
    ClassroomAndCourseData getClassroomSessionBySessionIds(@Param("sessionIds") List<Long> sessionIds);

    /**
     * 根据课堂id查询签到表，只有一个班级的，没有用到
     */
    List<Map<String,String>> getAttendanceBySessionId(@Param("courseId") Long courseId);

    /**
     * 根据课堂位置，上课时间查询课堂id，只能查到一个id，没有用到
     */
    Long findCourseSessionIdByLocationAndTime(@Param("courseLocation") String courseLocation,
                                                  @Param("sessionDate") LocalDate sessionDate,
                                                  @Param("time")LocalTime time);

    /**
     * 根据课堂位置，上课时间查询课堂id集合
     */
    List<Long> findCourseSessionIdsByLocationAndTime(@Param("courseLocation") String courseLocation,
                                                     @Param("sessionDate") LocalDate sessionDate,
                                                     @Param("time")LocalTime time);

    /**
     * 根据课堂id查询课堂的位置开始时间用来做签到判断
     */
    CourseInfo getCourseInfo(@Param("courseId") Long courseId);

    /**
     * 根据课堂id集合查询签到表可以查到所有上这个课的班级的
     */
    List<Map<String,String>> getAttendanceBySessionIds(@Param("sessionIds") List<Long> sessionIds);


//    Long findCourseSessionIdByLocationAndTime(@Param("courseLocation") String courseLocation,
//                                              @Param("sessionDate") LocalDate sessionDate,
//                                              @Param("time") LocalTime time);

    /**
     * 根据课程，班级，上课时间查询是否有这条数据
     */
    Long getClockInClassroomSession(ClockInClassroomSession classroomSession);

    /**
     * 添加数据
     */
    Long insertClockInClassroomSession(ClockInClassroomSession classroomSession);


    // 这个是给后台使用的 通过时间和班级名称和课堂名称查询出对应的课程id
    Long findSessionIdByClassAndCourse( @Param("className") String className,
                                        @Param("courseName") String courseName,
                                        @Param("sessionDate") LocalDate sessionDate,
                                        @Param("time")LocalTime time);
}

