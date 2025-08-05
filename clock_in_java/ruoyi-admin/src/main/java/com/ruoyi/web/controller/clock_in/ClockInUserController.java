package com.ruoyi.web.controller.clock_in;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.clock_in.domain.vo.ClockInLogVO;
import com.ruoyi.clock_in.domain.vo.UserAndClassVO;
import com.ruoyi.clock_in.domain.vo.UserTemplateVO;
import com.ruoyi.clock_in.service.IUserImportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.clock_in.domain.ClockInUser;
import com.ruoyi.clock_in.service.IClockInUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生Controller
 *
 * @author lzt
 * @date 2024-09-28
 */
@RestController
@RequestMapping("/clock_in/user")
public class ClockInUserController extends BaseController {
    @Resource
    private IClockInUserService clockInUserService;
    @Resource
    private IUserImportService userImportService;


    /**
     * 查询学生列表
     */
    @PreAuthorize("@ss.hasPermi('clock_in:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClockInUser clockInUser) {
        startPage();
        //List<ClockInUser> list = clockInUserService.selectClockInUserList(clockInUser);
        List<UserAndClassVO> list = clockInUserService.selectClockInUserAndClassList(clockInUser);
        return getDataTable(list);
    }

    /**
     * 导出学生列表
     */
    @PreAuthorize("@ss.hasPermi('clock_in:user:export')")
    @Log(title = "学生", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClockInUser clockInUser) {
        //List<ClockInUser> list = clockInUserService.selectClockInUserList(clockInUser);
        //ExcelUtil<ClockInUser> util = new ExcelUtil<ClockInUser>(ClockInUser.class);
        List<UserAndClassVO> list = clockInUserService.selectClockInUserAndClassList(clockInUser);
        ExcelUtil<UserAndClassVO> util = new ExcelUtil<>(UserAndClassVO.class);
        util.exportExcel(response, list, "学生数据");
    }

    @PreAuthorize("@ss.hasPermi('clock_in:user:download')")
    @Log(title = "学生", businessType = BusinessType.EXPORT)
    @PostMapping("/download")
    public void export(HttpServletResponse response) {
        ExcelUtil<UserTemplateVO> util = new ExcelUtil<>(UserTemplateVO.class);
        util.exportExcel(response, null, "模板文件");
    }

    /**
     * 获取学生详细信息
     */
    @PreAuthorize("@ss.hasPermi('clock_in:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(clockInUserService.selectClockInUserById(id));
    }

    /**
     * 新增学生
     */
    @PreAuthorize("@ss.hasPermi('clock_in:user:add')")
    @Log(title = "学生", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserAndClassVO clockInUser) {
        //return toAjax(clockInUserService.insertClockInUser(clockInUser));
        return toAjax(clockInUserService.saveUser(clockInUser));

    }

    /**
     * 修改学生
     */
    @PreAuthorize("@ss.hasPermi('clock_in:user:edit')")
    @Log(title = "学生", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserAndClassVO userAndClassVO) {
        return toAjax(clockInUserService.updateClockInUser(userAndClassVO));
    }

    /**
     * 删除学生
     */
    @PreAuthorize("@ss.hasPermi('clock_in:user:remove')")
    @Log(title = "学生", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(clockInUserService.deleteClockInUserByIds(ids));
    }
    //@PreAuthorize("@ss.hasPermi('clock_in:user:import')")
    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile file) {
        userImportService.importExcel(file);
    }
}
