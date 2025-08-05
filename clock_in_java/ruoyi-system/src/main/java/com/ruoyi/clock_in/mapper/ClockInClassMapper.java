package com.ruoyi.clock_in.mapper;

import com.ruoyi.clock_in.domain.ClockInClass;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Sovereign君
 * @ClassName ClockInClassMapper
 * @Date 2024/9/29 23:12
 * @Version V1.0
 */
public interface ClockInClassMapper {
    public Long getClassIdByName(String className);
    public int insertClassByName(ClockInClass clockInClass);

    List<Map<String,String>> findClassroomsAndClassesByDateAndTime(@Param("sessionDate") LocalDate sessionDate,
                                                                   @Param("time") LocalTime time);

    /**
     * 根据id查询班级名称
     * @param classId id
     * @return 班级名称
     */
    String getClasName(@Param("classId") Long classId);
}
