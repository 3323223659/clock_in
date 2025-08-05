package com.ruoyi.clock_in.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author Sovereign君
 * @ClassName ClassStudentLogDTO
 * @Date 2024/10/5 14:37
 * @Version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassStudentLogDTO  {
    private Integer pageNum;
    private Integer pageSize;
    private String className;
    private String courseName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
