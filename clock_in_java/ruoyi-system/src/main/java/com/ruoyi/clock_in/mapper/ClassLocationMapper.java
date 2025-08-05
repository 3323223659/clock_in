package com.ruoyi.clock_in.mapper;

import com.ruoyi.clock_in.domain.ClassLocationEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

import com.ruoyi.clock_in.domain.ClassLocation;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/30 下午9:35
 */
@Mapper
public interface ClassLocationMapper {

    ClassLocation selectLocation(@Param("name") String name);

    /**
     * 查询所有教室位置信息1
     */
    @Select("SELECT location_name FROM clock_in.clock_in_class_location where is_delete = 0")
    @Results({
            @Result(property = "locationName", column = "location_name")
    })
    List<ClassLocationEntity> selectAllClassLocation();

    Long insertLocation(ClassLocation location);
}