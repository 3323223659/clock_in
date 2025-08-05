package com.ruoyi.clock_in.service;

import com.ruoyi.clock_in.domain.vo.QRCodeVO;

import java.time.LocalDate;
import java.time.LocalTime;

public interface QRCodeService {

    //生成签到二维码
    QRCodeVO generateQRCode(String className, LocalDate sessionDate, LocalTime time);

}
