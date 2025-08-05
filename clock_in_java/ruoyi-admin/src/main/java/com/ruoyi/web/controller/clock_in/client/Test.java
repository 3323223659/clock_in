package com.ruoyi.web.controller.clock_in.client;

import com.ruoyi.clock_in.domain.ClassLocationEntity;
import com.ruoyi.clock_in.domain.ClassroomAndCourseData;
import com.ruoyi.clock_in.domain.StudentsAttendance;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.dto.ClockInAttendanceDTO;
import com.ruoyi.clock_in.domain.vo.ClassroomAttendance;
import com.ruoyi.clock_in.domain.vo.ClassroomSessionVO;
import com.ruoyi.clock_in.domain.vo.ClockInLogVO;
import com.ruoyi.clock_in.domain.vo.UserAndClassVO;
import com.ruoyi.clock_in.mapper.ClockInClassMapper;
import com.ruoyi.clock_in.mapper.ClockInClassroomSessionMapper;
import com.ruoyi.clock_in.mapper.ClockInLogMapper;
import com.ruoyi.clock_in.mapper.ClockInUserMapper;
import com.ruoyi.clock_in.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/28 下午5:31
 */
@RequestMapping("/app")
@RestController
public class Test {
    @Resource
    private ClockInUserMapper clockInUserMapper;
    @Resource
    private ClockInLogMapper clockInLogMapper;
    @Resource
    private ClockInClassMapper clockInClassMapper;
    @Resource
    private IClockInUserService clockInUserService;
    @Resource
    private IUserImportService userImportService;
    @Resource
    private IClassroomSessionService classroomSessionService;
    @Resource
    private ClockInClassroomSessionMapper clockInClassroomSessionMapper;
    @Resource
    private ClassLocationService classLocationService;
    @Resource
    private IClockInLogService clockInLogService;

    @GetMapping("/")
//    todo 这个是做什么的，
    public List<UserAndClassVO> test() {
        List<UserAndClassVO> userAndClassVOS = clockInUserMapper.selectClockInUserAndClassList(null);
        System.out.println(userAndClassVOS);
        return userAndClassVOS;
    }

    @GetMapping("/b")
//    todo 这个是做什么的，
    public List<ClockInLogVO> test2() {
        System.out.println("到了");
        List<ClockInLogVO> attendanceSummary = clockInLogMapper.getAttendanceSummary(null);
        System.out.println(attendanceSummary);
        return attendanceSummary;
    }

    @GetMapping("/c")
//    todo 这个是做什么的，
    public Long testc() {
        return clockInClassMapper.getClassIdByName("计算机23-1");
    }
    @PostMapping("/d")
    public String testd(@RequestBody UserAndClassVO userAndClassVO){
        System.out.println("前端的数据"+userAndClassVO);
        System.out.println(clockInUserService.saveUser(userAndClassVO));
        return "success";
    }
    @PostMapping("/e")
    public void teste(MultipartFile file){
        System.out.println("file = " + file.getOriginalFilename());
        userImportService.importExcel(file);
    }
    @PostMapping("/f")
    public List<String> testf(@RequestBody ClassroomSessionDTO classroomSessionDTO){
        return clockInUserService.selectBeginNeedClockInUser(classroomSessionDTO);
    }

    @PostMapping("/g")
    public ClassroomSessionVO testg(Long courseId){
        System.out.println("classroomSessionDTO = " + courseId);
        return  classroomSessionService.getClassroomSession(courseId);
    }
    @GetMapping("/i")
    public List<String> getAllClassLocation(){
        return classLocationService.selectAllClassLocation();
    }
    @GetMapping("/j")
    public Long testJ(){
        return clockInUserMapper.selectUserId("jun");
    }
    @PostMapping("/k")
    public Long testK(@RequestBody ClassroomSessionDTO classroomSessionDTO){
        return classroomSessionService.findCourseSessionIdByLocationAndTime(classroomSessionDTO);
    }
    @PostMapping("/l")
    public int testL(@RequestBody ClockInAttendanceDTO clockInAttendanceDTO, HttpServletRequest request){
        System.out.println("前端的数据是："+clockInAttendanceDTO);
        return clockInLogService.insertClockInAttendance(clockInAttendanceDTO,request);
    }
}
