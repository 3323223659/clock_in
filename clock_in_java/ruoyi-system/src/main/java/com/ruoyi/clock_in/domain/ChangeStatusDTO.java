package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/*
* 老师修改学生状态DTO
*/
@Data
public class ChangeStatusDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 学生id
     */
    private Long userId;
    /**
     * 课堂id
     */
    private Long sessionId;
    /**
     * 签到状态
     */
    private Integer status;
}
