package com.ruoyi.clock_in.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/30 下午9:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Course extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 任课老师名称
     */
    private String teacherName;

    /**
     * 逻辑删除
     */
    private Long isDelete;
}
