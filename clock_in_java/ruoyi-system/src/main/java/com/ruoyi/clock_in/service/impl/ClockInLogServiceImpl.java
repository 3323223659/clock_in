package com.ruoyi.clock_in.service.impl;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.ruoyi.clock_in.domain.CheckDTO;
import com.ruoyi.clock_in.domain.ClassStudentLogDTO;
import com.ruoyi.clock_in.domain.ClockInAttendance;
import com.ruoyi.clock_in.domain.CourseInfo;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.dto.ClockInAttendanceDTO;
import com.ruoyi.clock_in.domain.vo.ClassroomSessionVO;
import com.ruoyi.clock_in.domain.vo.ClockInLogVO;
import com.ruoyi.clock_in.domain.vo.StudentAttendanceLogVO;
import com.ruoyi.clock_in.mapper.ClockInClassroomSessionMapper;
import com.ruoyi.clock_in.mapper.ClockInLogMapper;
import com.ruoyi.clock_in.service.IClassroomSessionService;
import com.ruoyi.clock_in.service.IClockInClassService;
import com.ruoyi.clock_in.service.IClockInLogService;
import com.ruoyi.clock_in.service.IClockInUserService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Sovereign君
 * @ClassName ClockInLogServiceImpl
 * @Date 2024/9/29 16:33
 * @Version V1.0
 */
@Service
public class ClockInLogServiceImpl implements IClockInLogService {
    @Resource
    private ClockInLogMapper clockInLogMapper;
    @Resource
    private IClockInUserService clockInUserService;
    @Resource
    private ClockInClassroomSessionMapper clockInClassroomSessionMapper;
    @Resource
    private IClockInClassService clockInClassService;
    @Resource
    private IClassroomSessionService classroomSessionService;

    @Override
    public Integer userCheckIn(CheckDTO checkDTO, HttpServletRequest request) {
        // 获取客户端的IP地址
        String ip = IpUtils.getIpAddr(request);
        //校验二维码超时时间
        String flushTime = checkDTO.getFlushTime();
        LocalTime now = LocalTime.now();
        if (!isOutTime(flushTime,now)) {
            return 0;
        }
        List<Long> sessionIds = getListFromString(checkDTO.getSessionIds());
//        List<Long> sessionIds = checkDTO.getSessionIds();
//        List<Long> sessionIds = new ArrayList<>();
        if (sessionIds.isEmpty()){
           return 0;
        }
        Long userId = checkDTO.getUserId();
        Integer isPresent = clockInLogMapper.selectIsPresent(sessionIds.get(0), userId);
        if (isPresent != null) {
            return isPresent;
        }
        try {
            if (isPresent == 0) {
                return isPresent;
            }
        }catch (NullPointerException e){
            //防止报空指针异常，不做处理
        }
        CourseInfo courseInfo = clockInClassroomSessionMapper.getCourseInfo(sessionIds.get(0));
        Time startTime = courseInfo.getStartTime();
        //校验地理位置
        String[] studentLocation = checkDTO.getLocation().split(",");
        Double[] studentLatitudeAndLongitude = new Double[studentLocation.length];
        for (int i = 0; i < studentLocation.length; i++) {
            studentLatitudeAndLongitude[i] = Double.parseDouble(studentLocation[i]);
        }
//        Double[] sessionLongitude = {110.920595, 110.924010};  //经度范围
//        Double[] sessionLatitude = {21.676140, 21.678120};  //纬度范围
//        //判断是否在纬度范围内
//       if (!(studentLatitudeAndLongitude[0] >= sessionLongitude[0] && studentLatitudeAndLongitude[0] <= sessionLongitude[1] &&
//               studentLatitudeAndLongitude[1] >= sessionLatitude[0] && studentLatitudeAndLongitude[1] <= sessionLatitude[1])){
//           return 0;
//       }
       //校验是否迟到
        Time currentTime = Time.valueOf(now);
        System.out.println("当前时间: " + currentTime);
        ClockInAttendance clockInAttendance = new ClockInAttendance();
        clockInAttendance.setClassroomSessionId(sessionIds.get(0));
        clockInAttendance.setStudentId(userId);
        clockInAttendance.setLocation(courseInfo.getLocation());
        clockInAttendance.setIpAddress(ip);
        if (currentTime.after(startTime)) {
            System.out.println("当前时间在开始时间之后,迟到");
            clockInAttendance.setIsPresent(2);
            clockInLogMapper.insertClockInAttendance(clockInAttendance);
            return 2;
        } else {
            System.out.println("当前时间在开始时间之前，签到");
            clockInAttendance.setIsPresent(1);
            clockInLogMapper.insertClockInAttendance(clockInAttendance);
            return 1;
        }
    }

    /**
     * 根据课堂id与学生id查询对应的签到状态
     */
    @Override
    public Integer selectIsPresent(Long classroomSessionId, Long studentId) {
        if (classroomSessionId==null || studentId==null)
            return null;
        return clockInLogMapper.selectIsPresent(classroomSessionId,studentId);
    }

    /**
     * 根据课堂id与学生id修改对应的签到状态
     */
    @Override
    public Integer updateClockInAttendanceIsPresent(Integer isPresent,Long studentId,Long sessionId) {
        if (isPresent==null)
            return 0;
        return clockInLogMapper.updateClockInAttendanceIsPresent(isPresent,studentId,sessionId);
    }

    /**
     * 插入学生考勤记录
     */
    @Override
    public int insertClockInAttendance(ClockInAttendanceDTO clockInAttendanceDTO, HttpServletRequest httpServletRequest) {
        if (clockInAttendanceDTO.getIsPresent()==null ||
                StringUtils.isEmpty(clockInAttendanceDTO.getName())||
                StringUtils.isEmpty(clockInAttendanceDTO.getCourseLocation())){
            return -1;
        }
        Long userId = clockInUserService.selectUserIdByName(clockInAttendanceDTO.getName());
        ClockInAttendance clockInAttendance = new ClockInAttendance();
        clockInAttendance.setStudentId(userId);
        String ipAddr = IpUtils.getIpAddr(httpServletRequest);
        clockInAttendance.setIpAddress(ipAddr);
        clockInAttendance.setLocation(clockInAttendanceDTO.getCourseLocation());
        List<Long> sessionIds = getListFromString(clockInAttendanceDTO.getSessionIds());
        if (sessionIds.isEmpty()) {
            return -1;
        }
        Integer i = selectIsPresent(sessionIds.get(0), userId);

        if (i==null){
            clockInAttendance.setClassRoomSessionId(sessionIds.get(0));
            clockInAttendance.setIsPresent(clockInAttendanceDTO.getIsPresent());
            return  clockInLogMapper.insertClockInAttendance(clockInAttendance);
        }
        if (!Objects.equals(i, clockInAttendanceDTO.getIsPresent())){
            return updateClockInAttendanceIsPresent(clockInAttendanceDTO.getIsPresent(), userId,sessionIds.get(0));
        }


        return -1;
    }

    /**
     * 判断是否超时
     */
    private boolean isOutTime(String flushTime,LocalTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime flushLocalTime = LocalTime.parse(flushTime, formatter);
        // 计算两个时间之间的差异（秒）
        long differenceTime = ChronoUnit.SECONDS.between(flushLocalTime, now);
        return differenceTime < 800 && differenceTime > 0;
    }

    /**
     * 前端传的String类型的课堂ids转为list类型
     */
    private List<Long> getListFromString(String ids) {
        String cleanedIds = ids.replaceAll("[\\[\\]]", "");
        return Arrays.stream(cleanedIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * 查询打卡记录
     *
     * @param id 打卡记录主键
     * @return 打卡记录
     */
    @Override
    public ClockInLogVO selectClockInLogById(Long id) {
        return clockInLogMapper.selectClockInLogById(id);
    }

    /**
     * 查询打卡记录列表
     *
     * @param clockInLog 打卡记录
     * @return 打卡记录
     */
    @Override
    public List<ClockInLogVO> selectClockInLogList(ClockInLogVO clockInLog) {
        return clockInLogMapper.selectClockInLogList(clockInLog);
    }

    @Override
    public List<ClockInLogVO> getAttendanceSummary(ClockInLogVO clockInLog) {
        return clockInLogMapper.getAttendanceSummary(clockInLog);
    }

    /**
     * 新增打卡记录
     *
     * @param clockInLog 打卡记录
     * @return 结果
     */
    @Override
    public int insertClockInLog(ClockInLogVO clockInLog) {
        clockInLog.setCreateTime(DateUtils.getNowDate());
        return clockInLogMapper.insertClockInLog(clockInLog);
    }

    /**
     * 修改打卡记录
     *
     * @param clockInLog 打卡记录
     * @return 结果
     */
    @Override
    public int updateClockInLog(ClockInLogVO clockInLog) {
        clockInLog.setUpdateTime(DateUtils.getNowDate());
        return clockInLogMapper.updateClockInLog(clockInLog);
    }

    /**
     * 批量删除打卡记录
     *
     * @param ids 需要删除的打卡记录主键
     * @return 结果
     */
    @Override
    public int deleteClockInLogByIds(Long[] ids) {
        return clockInLogMapper.deleteClockInLogByIds(ids);
    }

    /**
     * 删除打卡记录信息
     *
     * @param id 打卡记录主键
     * @return 结果
     */
    @Override
    public int deleteClockInLogById(Long id) {
        return clockInLogMapper.deleteClockInLogById(id);
    }

    @Override
    public List<StudentAttendanceLogVO> findAttendanceByClassroomSessionIdAndClassId(Long classroomSessionId,Long classId) {
        // 检查课堂会话ID是否为null，如果为null，则返回空列表
        if (classroomSessionId==null||classId==null)
            return Collections.emptyList();

        // 通过课堂会话ID和班级id查询出勤记录
        List<StudentAttendanceLogVO> attendances = clockInLogMapper.findAttendanceByClassroomSessionIdAndClassId(classroomSessionId, classId);

        // 检查查询到的出勤记录是否为空，如果为空，则返回空列表
        if (attendances.isEmpty())
            return Collections.emptyList();

        // 返回查询到的出勤记录列表
        return attendances;
    }

    @Override
    public List<StudentAttendanceLogVO> list(ClassStudentLogDTO classStudentLogDTO) {
        // 根据班级和课程获取会话ID
        Long sessionIdByClassAndCourse = classroomSessionService.findSessionIdByClassAndCourse(classStudentLogDTO);
        System.out.println("课堂id："+sessionIdByClassAndCourse);
        if (sessionIdByClassAndCourse==null||sessionIdByClassAndCourse==0L)
            return Collections.emptyList();
        // 根据班级名称获取班级ID
        Long classId = clockInClassService.getClassIdByName(classStudentLogDTO.getClassName());
        System.out.println("班级id："+classId);
        if (classId==null)
            return Collections.emptyList();

        // 获取分页参数
        Integer pageSize = classStudentLogDTO.getPageSize();
        Integer pageNum = classStudentLogDTO.getPageNum();

        // 设置分页
        PageHelper.startPage(pageNum,pageSize);

        // 根据会话ID和班级ID获取打卡信息
        List<StudentAttendanceLogVO> attendances = findAttendanceByClassroomSessionIdAndClassId(sessionIdByClassAndCourse, classId);
        if (attendances.isEmpty())
            return Collections.emptyList();

        // 处理未打卡用户的打卡时间
        return attendances;
    }
}
