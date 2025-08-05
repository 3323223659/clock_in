package com.ruoyi.clock_in.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ClassLocationVo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 教室id
     */
    private Long id;
    /**
     * 教室名称
     */
    private String locationName;
}