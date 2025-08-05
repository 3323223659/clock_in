package com.ruoyi.web.controller.clock_in;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.clock_in.domain.ClassStudentLogDTO;
import com.ruoyi.clock_in.domain.vo.ClassAndCourseVO;
import com.ruoyi.clock_in.domain.vo.ClockInLogVO;
import com.ruoyi.clock_in.domain.vo.StudentAttendanceLogVO;
import com.ruoyi.clock_in.service.IClassroomSessionService;
import com.ruoyi.clock_in.service.IClockInClassService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.clock_in.service.IClockInLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 打卡记录Controller
 *
 * @author ruoyi
 * @date 2024-09-29
 */
@RestController
@RequestMapping("/clock_in/log")
@CrossOrigin(origins = "http://localhost")
public class ClockInLogController extends BaseController {
    @Autowired
    private IClockInLogService clockInLogService;
    @Resource
    private IClassroomSessionService classroomSessionService;
    @Resource
    private IClockInClassService clockInClassService;
    /**
     * 查询打卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('clock_in:log:list')")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody  ClassStudentLogDTO classStudentLogDTO) {
        List<StudentAttendanceLogVO> list = clockInLogService.list(classStudentLogDTO);
        if (list.isEmpty())
            return AjaxResult.warn("没有打卡记录");
        for (StudentAttendanceLogVO attendance : list) {
            if (!(attendance.getStatus().equals("已打卡"))){
                attendance.setClockInTime(null);
            }
        }

        // 封装分页信息
        PageInfo<StudentAttendanceLogVO> pageInfo = new PageInfo<>(list);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 导出打卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('clock_in:log:export')")
    @Log(title = "打卡记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ClassStudentLogDTO classStudentLogDTO) {
        List<StudentAttendanceLogVO> list = clockInLogService.list(classStudentLogDTO);

        // 设置 Content-Disposition 头
        response.setHeader("Content-Disposition", "attachment; filename=\"export.xlsx\"");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // 设置文件类型

        // 实现 Excel 导出
        ExcelUtil<StudentAttendanceLogVO> util = new ExcelUtil<>(StudentAttendanceLogVO.class);
        util.exportExcel(response, list, "打卡记录数据");
    }


    /**
     * 获取打卡记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('clock_in:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(clockInLogService.selectClockInLogById(id));
    }

    /**
     * 新增打卡记录
     */
    @PreAuthorize("@ss.hasPermi('clock_in:log:add')")
    @Log(title = "打卡记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClockInLogVO clockInLog) {
        return toAjax(clockInLogService.insertClockInLog(clockInLog));
    }

    /**
     * 修改打卡记录
     */
    @PreAuthorize("@ss.hasPermi('clock_in:log:edit')")
    @Log(title = "打卡记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClockInLogVO clockInLog) {
        return toAjax(clockInLogService.updateClockInLog(clockInLog));
    }

    /**
     * 删除打卡记录
     */
    @PreAuthorize("@ss.hasPermi('clock_in:log:remove')")
    @Log(title = "打卡记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(clockInLogService.deleteClockInLogByIds(ids));
    }

    @PostMapping("/getClassroomsAndClasses")
    public AjaxResult getClassroomsAndClasses(@RequestBody  ClassStudentLogDTO classStudentLogDTO){
        LocalDateTime time = classStudentLogDTO.getTime();
        if (time==null)
            return null;
        ClassAndCourseVO classAndCourseVO = clockInClassService.getClassroomsAndClasses(time.toLocalDate(), time.toLocalTime());
        System.out.println(classAndCourseVO);
        return success(classAndCourseVO);
    }
}
