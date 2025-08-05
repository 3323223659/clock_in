package com.ruoyi.clock_in.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author Sovereign君
 * @ClassName StudentAttendanceLogVO
 * @Date 2024/10/5 15:19
 * @Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceLogVO {
    private Long id;
    @Excel(name = "学号")
    private String username;  // 学生账号
    @Excel(name = "姓名")
    private String name;

    @Excel(name = "打卡状态")
    private String status; // 打卡状态
    // 姓名
    @Excel(name = "打卡时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clockInTime; // 打卡时间（如果未打卡返回null）

}
