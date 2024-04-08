package com.sztus.lib.back.end.basic.utils;


import com.sztus.lib.back.end.basic.type.constant.CommonConst;
import com.sztus.lib.back.end.basic.type.constant.GlobalConst;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Max
 * @date 2023/03/17
 */
public class HttpUtil {

    private HttpUtil() {
    }

    public static String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(ip) || CommonConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-for");
        }
        if (StringUtils.isEmpty(ip) || CommonConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || CommonConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || CommonConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        //it's possible to show the multiple ip addresses in forwarded ip
        if (StringUtils.isNotEmpty(ip) && ip.contains(GlobalConst.STR_COMMA)) {
            String[] ipAddressArray = ip.split(GlobalConst.STR_COMMA);
            int length = ipAddressArray.length - 1;
            ip = ipAddressArray[length - 1].trim();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
