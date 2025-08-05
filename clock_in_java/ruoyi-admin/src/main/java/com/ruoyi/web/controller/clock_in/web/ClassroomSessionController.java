package com.ruoyi.web.controller.clock_in.web;

import com.ruoyi.clock_in.domain.CheckDTO;
import com.ruoyi.clock_in.domain.StudentsAttendance;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.dto.ClockInAttendanceDTO;
import com.ruoyi.clock_in.domain.vo.ClassroomAttendance;
import com.ruoyi.clock_in.service.ClassLocationService;
import com.ruoyi.clock_in.service.IClassroomSessionService;
import com.ruoyi.clock_in.service.IClockInLogService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Sovereign君
 * @ClassName ClassroomSessionController
 * @Date 2024/10/2 16:34
 * @Version V1.0
 */
@RestController
@RequestMapping("/web/clock_in/v1/sessionAndCheckIn")
public class ClassroomSessionController {
    @Resource
    private IClassroomSessionService classroomSessionService;
    @Resource
    private IClockInLogService clockInLogService;
    @Resource
    private ClassLocationService classLocationService;

    /**
     * 学生签到
     *
     * @param checkDTO 课程信息
     */
    @PostMapping("/checkIn")
    public Integer checkIn(@RequestBody CheckDTO checkDTO, HttpServletRequest request) {
        System.out.println("checkIn前端传来的参数："+ checkDTO);
        return clockInLogService.userCheckIn(checkDTO, request);
//        return null;
    }

    /**
     * 根据选中的班级返回老师，学生状态
     *
     * @return - 任课老师名称，全部学生以及状态
     */
    @GetMapping("/getClassroomAttendance")
    public List<StudentsAttendance> getClassroomAttendance(String sessionIds) {
        System.out.println("getClassroomAttendance前端传来的参数：" + sessionIds);
        return classroomSessionService.getAttendanceByClassroomSession(sessionIds);
    }

    /**
     * 获取所有的教室
     */
    @GetMapping("/getAllClassLocation")
    public List<String> getAllClassLocation() {
        System.out.println("这里是：getAllClassLocation" + classLocationService.selectAllClassLocation());
        return classLocationService.selectAllClassLocation();
    }

    /**
     * 老师给学生修改签到状态
     */
    @PostMapping("/insertClockInAttendance")
    public int insertClockInAttendance(@RequestBody ClockInAttendanceDTO clockInAttendanceDTO, HttpServletRequest request) {
        System.out.println("这里是insertClockInAttendance：" + clockInAttendanceDTO);
        return clockInLogService.insertClockInAttendance(clockInAttendanceDTO, request);
    }
}
