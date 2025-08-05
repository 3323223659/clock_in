package com.ruoyi.clock_in.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import com.ruoyi.clock_in.domain.ClockInClass;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.vo.UserAndClassVO;
import com.ruoyi.clock_in.service.IClassroomSessionService;
import com.ruoyi.clock_in.service.IClockInClassService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import com.ruoyi.clock_in.mapper.ClockInUserMapper;
import com.ruoyi.clock_in.domain.ClockInUser;
import com.ruoyi.clock_in.service.IClockInUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 学生Service业务层处理
 *
 * @author lzt
 * @date 2024-09-28
 */
@Service
public class ClockInUserServiceImpl implements IClockInUserService {
    @Resource
    private IClockInClassService iClockInClassService;
    @Resource
    private ClockInUserMapper clockInUserMapper;
    @Resource
    private IClockInClassService clockInClassService;
    @Resource
    private IClassroomSessionService classroomSessionService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveUser(UserAndClassVO userAndClassVO) {
        if (StringUtils.isEmpty(userAndClassVO.getClassName()) ||
                StringUtils.isEmpty(userAndClassVO.getUsername()) ||
                StringUtils.isEmpty(userAndClassVO.getName())){
            return 0;
        }
        if (userAndClassVO.getCreateTime()==null){
            userAndClassVO.setCreateTime(DateUtils.getNowDate());
        }
        // 查询班级id
        Long classIdByName = iClockInClassService.getClassIdByName(userAndClassVO.getClassName());

        ClockInUser clockInUser = new ClockInUser();
        clockInUser.setUsername(userAndClassVO.getUsername());
        clockInUser.setName(userAndClassVO.getName());
        clockInUser.setPassword(userAndClassVO.getPassword());
        clockInUser.setCreateTime(userAndClassVO.getCreateTime());

        if (classIdByName == null){
            ClockInClass clockInClass = new ClockInClass();
            clockInClass.setClassName(userAndClassVO.getClassName());
            iClockInClassService.insertClassByName(clockInClass);

            clockInUser.setClassId(clockInClass.getId());

            return  clockInUserMapper.insertClockInUser(clockInUser);
        }else {
            clockInUser.setClassId(classIdByName);
             return clockInUserMapper.insertClockInUser(clockInUser);
        }

    }

    @Override
    public List<String> selectUserByClassId(Long classId) {
        return clockInUserMapper.selectUserByClassId(classId);
    }

    @Override
    public List<String> selectBeginNeedClockInUser(ClassroomSessionDTO classroomSessionDTO) {
        if (classroomSessionDTO==null)
            return null;
        if (classroomSessionDTO.getTime()==null)
            return null;
        LocalDateTime time = classroomSessionDTO.getTime();
        LocalDate localDate = time.toLocalDate();
        LocalTime localTime = time.toLocalTime();
        Long classIdByLocationAndTime = classroomSessionService.findClassIdByLocationAndTime(classroomSessionDTO.getCourseLocation(), localDate, localTime);
        if (classIdByLocationAndTime==null)
            return null;
        return clockInUserMapper.selectUserByClassId(classIdByLocationAndTime);
    }

    @Override
    public Long selectUserIdByName(String name) {
        if (StringUtils.isEmpty(name))
            return 0L;
        return clockInUserMapper.selectUserId(name);
    }

    /**
     * 查询学生
     *
     * @param id 学生主键
     * @return 学生
     */
    @Override
    public ClockInUser selectClockInUserById(Long id) {
        return clockInUserMapper.selectClockInUserById(id);
    }
    /**
     * 查询学生
     *
     * 查询学生所有信息列表
     * @return 学生
     */
    @Override
    public List<UserAndClassVO> selectClockInUserAndClassList(ClockInUser clockInUser) {
        return clockInUserMapper.selectClockInUserAndClassList(clockInUser);
    }

    /**
     * 查询学生列表
     *
     * @param clockInUser 学生
     * @return 学生
     */
    @Override
    public List<ClockInUser> selectClockInUserList(ClockInUser clockInUser) {
        return clockInUserMapper.selectClockInUserList(clockInUser);
    }

    /**
     * 新增学生
     *
     * @param clockInUser 学生
     * @return 结果
     */
    @Override
    public int insertClockInUser(ClockInUser clockInUser) {
        clockInUser.setCreateTime(DateUtils.getNowDate());
        return clockInUserMapper.insertClockInUser(clockInUser);
    }

    /**
     * 修改学生
     *
     * @param clockInUser 学生
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateClockInUser(UserAndClassVO clockInUser) {
        clockInUser.setUpdateTime(DateUtils.getNowDate());

        Long classIdByName = clockInClassService.getClassIdByName(clockInUser.getClassName());
        ClockInUser clockInUser1 = new ClockInUser();
        clockInUser1.setClassId(classIdByName);
        clockInUser1.setPassword(clockInUser.getPassword());
        clockInUser1.setName(clockInUser.getName());
        clockInUser1.setUsername(clockInUser.getUsername());
        clockInUser1.setUpdateTime(DateUtils.getNowDate());

        if (classIdByName==null){
            ClockInClass clockInClass = new ClockInClass();
            clockInClass.setClassName(clockInUser.getClassName());
            clockInClassService.insertClassByName(clockInClass);
            clockInUser1.setClassId(clockInClass.getId());
        }else {
            clockInUser1.setId(clockInUser.getId());
        }

        return clockInUserMapper.updateClockInUser(clockInUser1);
    }

    /**
     * 批量删除学生
     *
     * @param ids 需要删除的学生主键
     * @return 结果
     */
    @Override
    public int deleteClockInUserByIds(Long[] ids) {
        return clockInUserMapper.deleteClockInUserByIds(ids);
    }

    /**
     * 删除学生信息
     *
     * @param id 学生主键
     * @return 结果
     */
    @Override
    public int deleteClockInUserById(Long id) {
        return clockInUserMapper.deleteClockInUserById(id);
    }



}
