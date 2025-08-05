package com.ruoyi.clock_in.service;

import java.util.List;

import com.ruoyi.clock_in.domain.ClockInUser;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.vo.UserAndClassVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生Service接口
 *
 * @author lzt
 * @date 2024-09-28
 */
public interface IClockInUserService {
    public int saveUser(UserAndClassVO userAndClassVO);
    public List<String> selectUserByClassId(Long classId);
    public List<String> selectBeginNeedClockInUser(ClassroomSessionDTO classroomSessionDTO);

    public Long selectUserIdByName(String name);
    /**
     * 查询学生
     *
     * @param id 学生主键
     * @return 学生
     */
    public ClockInUser selectClockInUserById(Long id);

    public List<UserAndClassVO> selectClockInUserAndClassList(ClockInUser clockInUser);

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
    public int updateClockInUser(UserAndClassVO clockInUser);

    /**
     * 批量删除学生
     *
     * @param ids 需要删除的学生主键集合
     * @return 结果
     */
    public int deleteClockInUserByIds(Long[] ids);

    /**
     * 删除学生信息
     *
     * @param id 学生主键
     * @return 结果
     */
    public int deleteClockInUserById(Long id);


}
