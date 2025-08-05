package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
public class ClassLocation extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 上课教室
     */
    private String locationName;

    /**
     * 逻辑删除
     */
    private Long isDelete;
}
