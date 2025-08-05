package com.ruoyi.clock_in.service;

import com.ruoyi.clock_in.domain.ClockInClass;
import com.ruoyi.clock_in.domain.vo.ClassAndCourseVO;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @Author Sovereign君
 * @ClassName IClockInClassService
 * @Date 2024/9/29 23:22
 * @Version V1.0
 */
public interface IClockInClassService {
    public Long getClassIdByName(String className);
    public int insertClassByName(ClockInClass clockInClass);

    public ClassAndCourseVO getClassroomsAndClasses(LocalDate sessionDate, LocalTime checkTime);
}
