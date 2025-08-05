package com.ruoyi.clock_in.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Sovereign君
 * @ClassName IUserImportService
 * @Date 2024/9/30 12:59
 * @Version V1.0
 */
public interface IUserImportService {
    public void importExcel(MultipartFile file);
}
