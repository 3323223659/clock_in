package com.ruoyi.clock_in.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Sovereign君
 * @ClassName ClassAndCourseVO
 * @Date 2024/10/5 20:58
 * @Version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassAndCourseVO {
    private List<String> classNames;
    private List<String> courseNames;
}
