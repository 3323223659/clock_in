package com.ruoyi.clock_in.service.impl;

import com.ruoyi.clock_in.domain.ClassLocationEntity;
import com.ruoyi.clock_in.domain.vo.ClassLocationVo;
import com.ruoyi.clock_in.mapper.ClassLocationMapper;
import com.ruoyi.clock_in.service.ClassLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassLocationServiceImpl implements ClassLocationService {

    @Autowired
    private ClassLocationMapper classLocationMapper;

    @Override
    public List<String> selectAllClassLocation() {
        List<ClassLocationEntity> classLocationEntities = classLocationMapper.selectAllClassLocation();
        return classLocationEntities.stream().map(ClassLocationEntity::getLocationName).collect(Collectors.toList());
    }


}
