package com.ruoyi.clock_in.mapper;

import com.ruoyi.clock_in.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/30 下午9:03
 */
@Mapper
public interface CourseMapper {

    Course selectNameTeacher(@Param("name") String name, @Param("teacherName") String teacherName);

    int insertCourse(Course course);
}
