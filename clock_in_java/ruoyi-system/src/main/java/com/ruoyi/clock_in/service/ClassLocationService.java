package com.ruoyi.clock_in.service;

import com.ruoyi.clock_in.domain.ClassLocationEntity;
import com.ruoyi.clock_in.domain.vo.ClassLocationVo;

import java.time.LocalDateTime;
import java.util.List;

public interface ClassLocationService {

    List<String> selectAllClassLocation();
}
