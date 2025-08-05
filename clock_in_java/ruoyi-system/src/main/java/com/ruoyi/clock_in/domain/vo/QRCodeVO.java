package com.ruoyi.clock_in.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QRCodeVO extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 二维码
     */
    private String qrCode;
    /**
     * 班级
     */
    private String className;
    /**
     * 课程
     */
    private String sessionName;
    /**
     * 老师名称
     */
    private String teacherName;

}
