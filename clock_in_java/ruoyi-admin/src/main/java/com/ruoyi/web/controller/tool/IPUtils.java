package com.ruoyi.web.controller.tool;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Sovereign君
 * @ClassName IPUtils
 * @Date 2024/10/2 13:15
 * @Version V1.0
 */
public class IPUtils {
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对可能返回的多层代理情况进行处理
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim(); // 取第一个非 unknown 的地址
        }

        return ip;
    }
}
