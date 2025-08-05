package com.ruoyi.clock_in.mapper;

import java.util.List;

import com.ruoyi.clock_in.domain.ClockInAttendance;
import com.ruoyi.clock_in.domain.vo.ClockInLogVO;
import com.ruoyi.clock_in.domain.vo.StudentAttendanceLogVO;
import org.apache.ibatis.annotations.Param;

/**
 * 打卡记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-29
 */
public interface ClockInLogMapper {
    /**
     * 根据课堂id和学生id查询打卡记录（主要是更新同一个学生的
     *
     * @param classroomSessionId 课堂id
     * @param studentId          学生id
     * @return 打卡记录id
     */
    Integer selectIsPresent(@Param("classroomSessionId") Long classroomSessionId, @Param("studentId") Long studentId);

    /**
     * 更新同一个学生在同一个课堂的打卡记录
     *
     * @param isPresent - 打卡状态
     * @param studentId - 学生id
     * @param sessionId
     * @return 成功数量
     */
    Integer updateClockInAttendanceIsPresent(@Param("isPresent") int isPresent, @Param("studentId") Long studentId,@Param("sessionId") Long sessionId);

    /**
     * 学生打卡插入
     *
     * @param clockInAttendance 打卡记录
     * @return 数据成功条数
     */
    public int insertClockInAttendance(ClockInAttendance clockInAttendance);

    /**
     * 查询打卡记录
     *
     * @param id 打卡记录主键
     * @return 打卡记录
     */
    public ClockInLogVO selectClockInLogById(Long id);

    /**
     * 查询打卡记录列表
     *
     * @param clockInLogVO
     * @return
     */
    public List<ClockInLogVO> getAttendanceSummary(ClockInLogVO clockInLogVO);

    /**
     * 查询打卡记录列表
     *
     * @param clockInLog 打卡记录
     * @return 打卡记录集合
     */
    public List<ClockInLogVO> selectClockInLogList(ClockInLogVO clockInLog);

    /**
     * 新增打卡记录
     *
     * @param clockInLog 打卡记录
     * @return 结果
     */
    public int insertClockInLog(ClockInLogVO clockInLog);

    /**
     * 修改打卡记录
     *
     * @param clockInLog 打卡记录
     * @return 结果
     */
    public int updateClockInLog(ClockInLogVO clockInLog);

    /**
     * 删除打卡记录
     *
     * @param id 打卡记录主键
     * @return 结果
     */
    public int deleteClockInLogById(Long id);

    /**
     * 批量删除打卡记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClockInLogByIds(Long[] ids);

    List<StudentAttendanceLogVO> findAttendanceByClassroomSessionIdAndClassId(@Param("classroomSessionId") Long classroomSessionId,@Param("classId") Long classId);

}
