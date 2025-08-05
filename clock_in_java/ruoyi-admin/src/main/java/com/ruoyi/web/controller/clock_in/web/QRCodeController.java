package com.ruoyi.web.controller.clock_in.web;

import com.ruoyi.clock_in.domain.vo.QRCodeVO;
import com.ruoyi.clock_in.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/web/clock_in/v1/common")
public class QRCodeController {

    @Autowired
    private QRCodeService QRCodeService;

    /**
     * 生成二维码
     */
    @GetMapping("/getQrCode")
    public QRCodeVO generateQRCode(String className, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sessionDate, @DateTimeFormat(pattern = "HH:mm:ss") LocalTime time) {
        System.out.println("generateQRCode前端传来的参数："+"className: " + className + "sessionDate: " + sessionDate + "time: " + time);
        return QRCodeService.generateQRCode(className,sessionDate,time);
    }

}
