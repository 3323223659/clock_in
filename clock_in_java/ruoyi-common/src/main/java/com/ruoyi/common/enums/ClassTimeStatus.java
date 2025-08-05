package com.ruoyi.common.enums;

import org.apache.poi.ss.formula.functions.T;

import java.sql.Time;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/10/2 下午11:54
 */
public enum ClassTimeStatus {

    ONE("01", Time.valueOf("08:00:00")),
    TWO("02", Time.valueOf("09:40:00")),
    THREE("03", Time.valueOf("10:00:00")),
    FOUR("04", Time.valueOf("11:40:00")),
    FIVE("05", Time.valueOf("14:30:00")),
    SIX("06", Time.valueOf("16:10:00")),
    SEVEN("07", Time.valueOf("16:20:00")),
    EIGHT("08", Time.valueOf("17:50:00")),
    NINE("09", Time.valueOf("19:40:00")),
    TEN("10", Time.valueOf("21:20:00")),


    ;

    private final String code;
    private final Time info;

    ClassTimeStatus(String code, Time info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public Time getInfo() {
        return info;
    }

    public static  ClassTimeStatus getClassTimeStatus(String code) {
        for (ClassTimeStatus classTimeStatus : ClassTimeStatus.values()) {
            if (classTimeStatus.getCode().equals(code)) {
                return classTimeStatus;
            }
        }
        return null;
    }

}
