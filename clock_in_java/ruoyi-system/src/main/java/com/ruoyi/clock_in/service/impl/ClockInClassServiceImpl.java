package com.ruoyi.clock_in.service.impl;

import com.ruoyi.clock_in.domain.ClockInClass;
import com.ruoyi.clock_in.domain.vo.ClassAndCourseVO;
import com.ruoyi.clock_in.mapper.ClockInClassMapper;
import com.ruoyi.clock_in.service.IClockInClassService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * @Author Sovereign君
 * @ClassName ClockInClassServiceImpl
 * @Date 2024/9/29 23:23
 * @Version V1.0
 */
@Service
public class ClockInClassServiceImpl implements IClockInClassService {
    @Resource
    private ClockInClassMapper clockInClassMapper;
    @Override
    public Long getClassIdByName(String className) {
        if (StringUtils.isEmpty(className))
            return null;
        return clockInClassMapper.getClassIdByName(className);
    }

    @Override
    public int insertClassByName(ClockInClass clockInClass) {
        if (StringUtils.isEmpty(clockInClass.getClassName()))
            return 0;
        return clockInClassMapper.insertClassByName(clockInClass);
    }

    @Override
    public ClassAndCourseVO getClassroomsAndClasses(LocalDate sessionDate, LocalTime checkTime) {
        if (sessionDate == null || checkTime == null)
            return null;
        List<Map<String, String>> results = clockInClassMapper.findClassroomsAndClassesByDateAndTime(sessionDate, checkTime);
        if (results == null)
            return null;
        Set<String> classNames = new HashSet<>();
        Set<String> courseNames = new HashSet<>();

        for (Map<String, String> result : results) {
            classNames.add(result.get("className"));
            courseNames.add(result.get("courseName"));
        }
        return new ClassAndCourseVO(new ArrayList<>(classNames), new ArrayList<>(courseNames));
    }
}
