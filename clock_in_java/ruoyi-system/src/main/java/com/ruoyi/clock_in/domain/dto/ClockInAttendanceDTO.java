package com.ruoyi.clock_in.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @Author Sovereign君
 * @ClassName ClockInAttendanceDTO
 * @Date 2024/10/2 13:28
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockInAttendanceDTO {
    private String name;
    private String courseLocation;
    private Integer isPresent;
    private String sessionIds;
}
