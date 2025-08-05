package com.ruoyi.clock_in.service.impl;

import com.ruoyi.clock_in.domain.vo.UserAndClassVO;
import com.ruoyi.clock_in.service.IClockInUserService;
import com.ruoyi.clock_in.service.IUserImportService;
import com.ruoyi.common.utils.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Sovereign君
 * @ClassName UserImportServiceImpl
 * @Date 2024/9/30 13:00
 * @Version V1.0
 */
@Service
public class UserImportServiceImpl implements IUserImportService {
    @Resource
    private IClockInUserService clockInUserService;
    @Override
    public void importExcel(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, String> fieldMapping = getFieldMapping(headerRow);

            List<UserAndClassVO> users = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // 跳过空行

                UserAndClassVO user = new UserAndClassVO();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) continue; // 跳过空单元格

                    String columnName = headerRow.getCell(j).getStringCellValue();
                    String fieldName = fieldMapping.get(columnName);
                    //System.out.println("columnName是"+columnName+",fieldName是："+fieldName+",具体数据是："+cell.getStringCellValue());

                    if (fieldName != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                switch (fieldName) {
                                    case "username":
                                        user.setUsername(cell.getStringCellValue());
                                        break;
                                    case "name":
                                        user.setName(cell.getStringCellValue());
                                        break;
                                    case "className":
                                        user.setClassName(cell.getStringCellValue());
                                        break;
                                }
                                break;
                            case NUMERIC:
                                if (fieldName.equals("username")){
                                    user.setUsername(String.valueOf((int) cell.getNumericCellValue()));
                                }
//                                if ("id".equals(fieldName)) {
//                                    user.setId((long) cell.getNumericCellValue());
//                                }
                                break;
                            case BLANK:
                                // 处理空字段
                                break;
                            default:
                                break;
                        }
                    }
                }
                users.add(user);
            }
            // 将用户插入数据库
            for (UserAndClassVO user : users) {
                user.setCreateTime(DateUtils.getNowDate());
                clockInUserService.saveUser(user);

            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Map<String, String> getFieldMapping(Row headerRow) {
        Map<String, String> fieldMapping = new HashMap<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String columnName = cell.getStringCellValue();
                switch (columnName) {
                    case "姓名":
                        fieldMapping.put(columnName, "name");
                        break;
                    case "学号":
                        fieldMapping.put(columnName, "username");
                        break;
                    case "密码":
                        fieldMapping.put(columnName, "password");
                        break;
                    case "班级":
                        fieldMapping.put(columnName, "className");
                        break;
                    // 添加更多字段的映射
                }
            }
        }
        return fieldMapping;
    }
}
