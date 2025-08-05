package com.ruoyi.clock_in.service.impl;

import com.alibaba.fastjson2.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruoyi.clock_in.domain.ClassroomAndCourseData;
import com.ruoyi.clock_in.domain.vo.QRCodeVO;
import com.ruoyi.clock_in.mapper.ClockInClassroomSessionMapper;
import com.ruoyi.clock_in.service.QRCodeService;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    @Autowired
    private ClockInClassroomSessionMapper clockInClassroomSessionMapper;

    //生成签到二维码
    @Override
    public QRCodeVO generateQRCode(String className, LocalDate sessionDate, LocalTime time){
        List<Long> sessionIds = clockInClassroomSessionMapper.findCourseSessionIdsByLocationAndTime(className,sessionDate,time);
        if (sessionIds.isEmpty()){
            return null;
        }
        //二维码信息生成
        String qrCode = getQRCode(sessionIds);
        ClassroomAndCourseData classroomSession = clockInClassroomSessionMapper.getClassroomSessionBySessionIds(sessionIds);
        if (classroomSession == null){
            return null;
        }
        return new QRCodeVO(qrCode,classroomSession.getClassName(),classroomSession.getCourseName(), classroomSession.getTeacherName());
    }

    private String getQRCode(List<Long> sessionIds) {
        Map<String, Object> dataMap = new HashMap<>();
        String flushTime = LocalDateTime.now().toString();
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        dataMap.put("sessionIds", sessionIds);
        dataMap.put("flushTime", flushTime);
        dataMap.put("uuid", uuidString);
        String jsonString = JSON.toJSONString(dataMap);
        try {
            HashMap<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            map.put(EncodeHintType.CHARACTER_SET, "utf-8");
            map.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(jsonString, BarcodeFormat.QR_CODE, 2000, 2000, map);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            return Base64.encodeBase64String(os.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
}
