package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class CheckDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 用户ID */
    private Long userId;
    /** 课堂ID */
    private String sessionIds;
    /** 地址 */
    private String location;
    /** 二维码刷新时间 */
    private String flushTime;

}
