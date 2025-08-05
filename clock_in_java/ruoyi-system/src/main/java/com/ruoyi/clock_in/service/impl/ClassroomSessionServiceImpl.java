package com.ruoyi.clock_in.service.impl;

import com.ruoyi.clock_in.domain.ClassroomAndCourseData;
import com.ruoyi.clock_in.domain.StudentsAttendance;
import com.ruoyi.clock_in.domain.*;
import com.ruoyi.clock_in.domain.dto.ClassroomSessionDTO;
import com.ruoyi.clock_in.domain.vo.ClassroomAttendance;
import com.ruoyi.clock_in.domain.vo.ClassroomSessionVO;
import com.ruoyi.clock_in.domain.vo.ClockInClassroomSessionVo;
import com.ruoyi.clock_in.mapper.*;
import com.ruoyi.clock_in.service.IClassroomSessionService;
import com.ruoyi.common.enums.ClassTimeStatus;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.common.constant.ClassConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/30 下午8:23
 */
@Service
public class ClassroomSessionServiceImpl implements IClassroomSessionService {
    @Resource
    private ClockInClassroomSessionMapper clockInClassroomSessionMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private ClassLocationMapper classLocationMapper;

    @Resource
    private ClockInClassMapper clockInClassMapper;

    @Resource
    private ClockInLogMapper clockInLogMapper;

    @Resource
    private ClockInUserMapper clockInUserMapper;

    /**
     * testController的，没有用到
     */
    @Override
    public Long findClassIdByLocationAndTime(String courseLocation, LocalDate sessionDate, LocalTime time) {
        if (StringUtils.isEmpty(courseLocation) || sessionDate == null || time == null)
            return 0L;
        return clockInClassroomSessionMapper.findClassIdByLocationAndTime(courseLocation, sessionDate, time);

    }

    /**
     * testController的，没有用到
     */
    @Override
    public ClassroomSessionVO getClassroomSession(Long courseId) {
        if (courseId == null)
            return null;
        ClassroomAndCourseData classroomSession = clockInClassroomSessionMapper.getClassroomSession(courseId);
        String[] split = classroomSession.getStudentNames().split("--");
        List<String> list = Arrays.asList(split);
        ClassroomSessionVO classroomSessionVO = new ClassroomSessionVO();
        classroomSessionVO.setClassName(classroomSession.getClassName());
        classroomSessionVO.setCourseName(classroomSession.getCourseName());
        classroomSessionVO.setTeacherName(classroomSession.getTeacherName());
        classroomSessionVO.setStudentNames(list);
        return classroomSessionVO;
    }

    /**
     * 根据课堂ids查询所有学生以及其对应签到状态
     */
    @Override
    public List<StudentsAttendance> getAttendanceByClassroomSession(String ids) {
        String cleanedIds = ids.replaceAll("[\\[\\]]", "");
        List<Long> sessionIds = Arrays.stream(cleanedIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Map<String, String>> results = clockInClassroomSessionMapper.getAttendanceBySessionIds(sessionIds);
        if (results.isEmpty()) {
            return null;
        }
        // 收集所有学生信息
        List<StudentsAttendance> students = new ArrayList<>();
        for (Map<String, String> result : results) {
            String studentName = result.get("studentName");
            String status = result.get("status");
            students.add(new StudentsAttendance(studentName, status));
        }
        return students;
    }

    /**
     * testController的，没有用到
     */
    @Override
    public Long findCourseSessionIdByLocationAndTime(ClassroomSessionDTO classroomSessionDTO) {
        if (classroomSessionDTO == null)
            return 0L;
        if (StringUtils.isEmpty(classroomSessionDTO.getCourseLocation()) || classroomSessionDTO.getTime() == null)
            return 0L;
        LocalDateTime localDateTime = classroomSessionDTO.getTime();
        LocalDate sessionDate = localDateTime.toLocalDate();
        LocalTime time = localDateTime.toLocalTime();
        return clockInClassroomSessionMapper.findCourseSessionIdByLocationAndTime(classroomSessionDTO.getCourseLocation(), sessionDate, time);
    }

    /**
     * 没有用到
     */
    @Override
    public List<Long> findCourseSessionIdsByLocationAndTime(ClassroomSessionDTO classroomSessionDTO) {
        if (classroomSessionDTO == null)
            return null;
        if (StringUtils.isEmpty(classroomSessionDTO.getCourseLocation()) || classroomSessionDTO.getTime() == null)
            return null;
        LocalDateTime localDateTime = classroomSessionDTO.getTime();
        LocalDate sessionDate = localDateTime.toLocalDate();
        LocalTime time = localDateTime.toLocalTime();
        return clockInClassroomSessionMapper.findCourseSessionIdsByLocationAndTime(classroomSessionDTO.getCourseLocation(), sessionDate, time);
    }

    /**
     * 解析json课堂数据
     *
     * @param data - 数据json
     */
    @Override
    @Transactional
    public List<ClockInClassroomSessionVo> analyticSession(String data, String username) {
        ClockInUser clockInUser = clockInUserMapper.selectClockInUserUserName(username);
        if (Objects.isNull(clockInUser)) {
            return null;
        }

        List<ClockInClassroomSessionVo> classroomSessionVoList = new ArrayList<>();
        JSONArray objects = new JSONArray(data);
        Map<String, String> dateMap = parseDateMap(objects.getJSONArray(1));

        JSONArray innerArray = objects.getJSONArray(0);
        for (int j = 0; j < innerArray.length(); j++) {
            JSONObject jsonObject = innerArray.getJSONObject(j);
            ClockInClassroomSessionVo clockInClassroomSessionVo = parseSessionVo(jsonObject, dateMap, clockInUser);
            if (clockInClassroomSessionVo != null) {
                classroomSessionVoList.add(clockInClassroomSessionVo);
            }
        }
        classroomSessionVoList.sort(Comparator
                .comparing((ClockInClassroomSessionVo vo) -> LocalDate.parse(vo.getSessionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .thenComparing(vo -> LocalTime.parse(vo.getStartTime(), DateTimeFormatter.ofPattern("HH:mm:ss"))));

        return classroomSessionVoList;
    }

    // 这个方法是给后台使用的，通过时间和班级名称和课堂名称查询出对应的课程id
    @Override
    public Long findSessionIdByClassAndCourse(ClassStudentLogDTO classStudentLogDTO) {
        // 检查班级名称、课堂名称和时间是否都已提供，如果任一条件未提供，则返回0
        if (StringUtils.isEmpty(classStudentLogDTO.getClassName()) ||
                StringUtils.isEmpty(classStudentLogDTO.getCourseName()) ||
                classStudentLogDTO.getTime() == null) {
            return 0L;
        }

        // 将时间对象分解为日期和时间部分，以便查询
        LocalDateTime localDateTime = classStudentLogDTO.getTime();
        LocalDate sessionDate = localDateTime.toLocalDate();
        LocalTime time = localDateTime.toLocalTime();

        // 调用mapper方法，根据班级名称、课堂名称、日期和时间查询课程id
        Long sessionIdByClassAndCourse = clockInClassroomSessionMapper.findSessionIdByClassAndCourse(classStudentLogDTO.getClassName(), classStudentLogDTO.getCourseName(), sessionDate, time);

        // 如果查询结果为空，则返回0，否则返回查询到的课程id
        if (sessionIdByClassAndCourse == null)
            return 0L;
        return sessionIdByClassAndCourse;
    }


    /**
     * 解析日期
     */
    private Map<String, String> parseDateMap(JSONArray datesArray) {
        Map<String, String> dateMap = new HashMap<>();
        for (int i = 0; i < datesArray.length(); i++) {
            JSONObject date = datesArray.getJSONObject(i);
            String xqmc = date.getString("xqmc");
            String rq = date.getString("rq");
            dateMap.put(xqmc, rq);
        }
        return dateMap;
    }


    /**
     * 解析json
     * @param jsonObject - json字符
     * @param dateMap - 时间
     * @param clockInUser - 用户信息
     * @return - 响应信息
     */
    private ClockInClassroomSessionVo parseSessionVo(JSONObject jsonObject, Map<String, String> dateMap, ClockInUser clockInUser) {
        // 课程名称
        String courseName = jsonObject.getString(ClassConstants.COURSE_NAME);
        // 老师名称
        String teacher = jsonObject.getString(ClassConstants.TEACHER);
        // 上课班级
        String classS = jsonObject.getString(ClassConstants.CLASS_S);
        // 上下课时间
        String sessionDate = jsonObject.getString(ClassConstants.SESSION_DATE);
        // 周几
        String date = jsonObject.getString(ClassConstants.SESSION);
        // 上课地点
        String courseLocation = jsonObject.getString(ClassConstants.COURSE_LOCATION);

        Course course = getOrCreateCourse(courseName, teacher);
        ClassLocation classLocation = getOrCreateClassLocation(courseLocation);
        Long[] classList = parseClassList(classS);

        Long sessionId = null;
        for (Long classId : classList) {
            ClockInClassroomSession session = createSession(course, classLocation, classId, dateMap, sessionDate, date);
            getOrCreateSessionId(session, clockInUser);
            if (Objects.equals(classId, clockInUser.getClassId())) {
                sessionId =  session.getId();
            }
        }
        return createSessionVo(course, courseLocation, dateMap, date, sessionDate, sessionId, clockInUser);
    }

    /**
     * 查询课程，如果没有就添加
     * @param courseName 课程名称
     * @param teacher 老师
     * @return 课程
     */
    private Course getOrCreateCourse(String courseName, String teacher) {
        Course course = courseMapper.selectNameTeacher(courseName, teacher);
        if (Objects.isNull(course)) {
            course = new Course();
            course.setCourseName(courseName);
            course.setTeacherName(teacher);
            course.setCreateTime(new Date());
            course.setUpdateTime(new Date());
            course.setIsDelete(0L);
            courseMapper.insertCourse(course);
            course.setId(courseMapper.selectNameTeacher(courseName, teacher).getId());
        }
        return course;
    }

    /**
     * 查询上课地点
     * @param courseLocation - 上课地点
     * @return - 上课地点
     */
    private ClassLocation getOrCreateClassLocation(String courseLocation) {
        ClassLocation classLocation = classLocationMapper.selectLocation(courseLocation);
        if (Objects.isNull(classLocation)) {
            classLocation = new ClassLocation();
            classLocation.setLocationName(courseLocation);
            classLocation.setCreateTime(new Date());
            classLocation.setUpdateTime(new Date());
            classLocation.setIsDelete(0L);
            classLocationMapper.insertLocation(classLocation);
            classLocation.setId(classLocationMapper.selectLocation(courseLocation).getId());
        }
        return classLocation;
    }


    private Long[] parseClassList(String classS) {
        String[] split = classS.split(",");
        Long[] classList = new Long[split.length];
        for (int z = 0; z < split.length; z++) {
            Long classIdByName = clockInClassMapper.getClassIdByName(split[z]);
            if (Objects.isNull(classIdByName)) {
                classIdByName = createClass(split[z]);
            }
            classList[z] = classIdByName;
        }
        return classList;
    }

    private Long createClass(String className) {
        ClockInClass clockInClass = new ClockInClass();
        clockInClass.setClassName(className);
        clockInClass.setCreateTime(new Date());
        clockInClass.setUpdateTime(new Date());
        clockInClass.setIsDelete(0);
        return (long) clockInClassMapper.insertClassByName(clockInClass);
    }

    private ClockInClassroomSession createSession(Course course, ClassLocation classLocation, Long classId, Map<String, String> dateMap, String sessionDate, String date) {
        ClockInClassroomSession session = new ClockInClassroomSession();
        session.setCourseId(course.getId());
        session.setCourseLocation(classLocation.getLocationName());
        session.setClassId(classId);
        session.setSessionDate(LocalDate.parse(dateMap.get(date)));
        session.setStartTime(ClassTimeStatus.getClassTimeStatus(sessionDate.split(",")[0]).getInfo().toLocalTime());
        session.setEndTime(ClassTimeStatus.getClassTimeStatus(sessionDate.split(",")[1]).getInfo().toLocalTime());
        return session;
    }

    /**
     *
     * @param session 课堂记录
     * @param clockInUser - 用户信息
     * @return 匹配相对于的课堂记录，并返回课堂id
     */
    private void getOrCreateSessionId(ClockInClassroomSession session, ClockInUser clockInUser) {
        Long existingSessionId = clockInClassroomSessionMapper.getClockInClassroomSession(session);
        if (Objects.isNull(existingSessionId)) {
            clockInClassroomSessionMapper.insertClockInClassroomSession(session);
            session.setId(clockInClassroomSessionMapper.getClockInClassroomSession(session));
        } else {
            session.setId(existingSessionId);
        }
    }

    /**
     * 创建返回示例
     * @param course 课程
     * @param courseLocation 上课地点
     * @param dateMap - 时间
     * @param date - 周次
     * @param sessionDate - 上下课时间
     * @param sessionId - 课堂记录id
     * @param clockInUser - 用户信息
     * @return 返回示例
     */
    private ClockInClassroomSessionVo createSessionVo(Course course,
                                                      String courseLocation,
                                                      Map<String, String> dateMap,
                                                      String date,
                                                      String sessionDate,
                                                      Long sessionId,
                                                      ClockInUser clockInUser) {
        ClockInClassroomSessionVo clockInClassroomSessionVo = new ClockInClassroomSessionVo();
        clockInClassroomSessionVo.setId(sessionId);
        clockInClassroomSessionVo.setCourseId(course.getId());
        clockInClassroomSessionVo.setCourseName(course.getCourseName());
        clockInClassroomSessionVo.setCourseLocation(courseLocation);
        clockInClassroomSessionVo.setSessionDate(dateMap.get(date));
        clockInClassroomSessionVo.setStartTime(ClassTimeStatus.getClassTimeStatus(sessionDate.split(",")[0]).getInfo().toString());
        clockInClassroomSessionVo.setEndTime(ClassTimeStatus.getClassTimeStatus(sessionDate.split(",")[1]).getInfo().toString());
        clockInClassroomSessionVo.setAttendanceStatus(0);

        if (Objects.nonNull(sessionId)) {
            Integer userInLog = clockInLogMapper.selectIsPresent(sessionId, clockInUser.getId());
            if (Objects.nonNull(userInLog)) {
                clockInClassroomSessionVo.setAttendanceStatus(Math.toIntExact(userInLog));
            }
        }
        System.out.println(clockInClassroomSessionVo);
        return clockInClassroomSessionVo;
    }


}
