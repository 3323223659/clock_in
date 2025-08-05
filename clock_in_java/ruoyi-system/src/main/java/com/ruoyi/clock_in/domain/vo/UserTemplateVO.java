package com.ruoyi.clock_in.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * @Author Sovereign君
 * @ClassName UserTemplateVO
 * @Date 2024/9/30 18:53
 * @Version V1.0
 */
@Data
public class UserTemplateVO {
    @Excel(name = "学号")
    private String username;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "班级")
    private String className;
}
