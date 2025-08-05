package com.ruoyi.clock_in.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author Sovereign君
 * @ClassName ClassroomSessionDTO
 * @Date 2024/10/1 12:55
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomSessionDTO {
    private String courseLocation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
