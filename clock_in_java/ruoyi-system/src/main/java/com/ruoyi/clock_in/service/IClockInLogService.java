package com.ruoyi.clock_in.service;

import com.ruoyi.clock_in.domain.CheckDTO;
import com.ruoyi.clock_in.domain.ClassStudentLogDTO;
import com.ruoyi.clock_in.domain.dto.ClockInAttendanceDTO;
import com.ruoyi.clock_in.domain.vo.ClockInLogVO;
import com.ruoyi.clock_in.domain.vo.StudentAttendanceLogVO;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @Author Sovereign君
 * @ClassName IClockInLogService
 * @Date 2024/9/29 17:48
 * @Version V1.0
 */
public interface IClockInLogService {

    //用户签到
    Integer userCheckIn(CheckDTO checkDTO, HttpServletRequest request);
    /**
     * 根据课堂id和学生id查询打卡记录（主要是更新同一个学生的
     */
    public Integer selectIsPresent(Long classroomSessionId, Long studentId);
    /**
     * 更新同一个学生在同一个课堂的打卡记录
     */
    public Integer updateClockInAttendanceIsPresent(Integer isPresent,Long studentId,Long sessionId);
    /**
     * 插入学生打卡记录
     */
    public int insertClockInAttendance(ClockInAttendanceDTO clockInAttendanceDTO, HttpServletRequest httpServletRequest);

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
     * @param clockInLog 打卡记录
     * @return 打卡记录集合
     */
    public List<ClockInLogVO> selectClockInLogList(ClockInLogVO clockInLog);

    public List<ClockInLogVO> getAttendanceSummary(ClockInLogVO clockInLog);

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
     * 批量删除打卡记录
     *
     * @param ids 需要删除的打卡记录主键集合
     * @return 结果
     */
    public int deleteClockInLogByIds(Long[] ids);

    /**
     * 删除打卡记录信息
     *
     * @param id 打卡记录主键
     * @return 结果
     */
    public int deleteClockInLogById(Long id);

    //根据课堂id查询学生签到记录
    List<StudentAttendanceLogVO> findAttendanceByClassroomSessionIdAndClassId( Long classroomSessionId,Long classId);

    List<StudentAttendanceLogVO> list(ClassStudentLogDTO classStudentLogDTO);

}
