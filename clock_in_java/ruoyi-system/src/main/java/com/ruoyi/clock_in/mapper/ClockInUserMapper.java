package com.ruoyi.clock_in.mapper;

import java.util.List;

import com.ruoyi.clock_in.domain.ClockInUser;
import com.ruoyi.clock_in.domain.vo.UserAndClassVO;
import org.apache.ibatis.annotations.Param;

/**
 * 学生Mapper接口
 *
 * @author lzt
 * @date 2024-09-28
 */
public interface ClockInUserMapper {

    /**
     * 根据学生姓名查询学生的id
     *
     * @param name 学生姓名
     * @return id
     */
    Long selectUserId(@Param("name") String name);

    /**
     * 查询学生
     *
     * @param id 学生主键
     * @return 学生
     */
    public ClockInUser selectClockInUserById(Long id);

    public List<UserAndClassVO> selectClockInUserAndClassList(ClockInUser clockInUser);

    public List<String> selectUserByClassId(Long classId);

    /**
     * 查询学生
     *
     * @param username - 学生账号
     * @return ： 学生
     */
    public ClockInUser selectClockInUserUserName(@Param("username") String username);

    /**
     * 查询学生列表
     *
     * @param clockInUser 学生
     * @return 学生集合
     */
    public List<ClockInUser> selectClockInUserList(ClockInUser clockInUser);

    /**
     * 新增学生
     *
     * @param clockInUser 学生
     * @return 结果
     */
    public int insertClockInUser(ClockInUser clockInUser);

    /**
     * 修改学生
     *
     * @param clockInUser 学生
     * @return 结果
     */
    public int updateClockInUser(ClockInUser clockInUser);

    /**
     * 删除学生
     *
     * @param id 学生主键
     * @return 结果
     */
    public int deleteClockInUserById(Long id);

    /**
     * 批量删除学生
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClockInUserByIds(Long[] ids);
}
