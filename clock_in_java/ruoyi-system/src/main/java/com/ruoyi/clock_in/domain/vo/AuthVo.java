package com.ruoyi.clock_in.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/29 下午9:52
 */
@Data
public class AuthVo {

    private Long id;

    private String name;

    private String username;

    private String className;

    private String token;

    private String msg;

    private List<ClockInClassroomSessionVo> resData;
}
