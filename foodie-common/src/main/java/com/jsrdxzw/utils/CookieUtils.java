package com.jsrdxzw.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * @author xuzhiwei
 */
public final class CookieUtils {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * @param request
     * @param cookieName
     * @return
     * @Description: 得到Cookie的值, 不编码
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * @param request
     * @param cookieName
     * @param isDecoder
     * @return
     * @Description: 得到Cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        for (Cookie cookie : cookieList) {
            if (cookie.getName().equals(cookieName)) {
                if (isDecoder) {
                    retValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                } else {
                    retValue = cookie.getValue();
                }
                break;
            }
        }
        return retValue;
    }

    /**
     * @param request
     * @param cookieName
     * @param encodeString
     * @return
     * @Description: 得到Cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookie.getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @Description: 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage
     * @Description: 设置Cookie的值 在指定时间内生效,但不编码
     */
    private static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param isEncode
     * @Description: 设置Cookie的值 不设置生效时间,但编码
     * 在服务器被创建，返回给客户端，并且保存客户端
     * 如果设置了SETMAXAGE(int seconds)，会把cookie保存在客户端的硬盘中
     * 如果没有设置，会默认把cookie保存在浏览器的内存中
     * 一旦设置setPath()：只能通过设置的路径才能获取到当前的cookie信息
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage
     * @param isEncode
     * @Description: 设置Cookie的值 在指定时间内生效, 编码参数
     */
    private static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage
     * @param encodeString
     * @Description: 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @Description: 删除Cookie带cookie域名
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, null, -1, false);
//        doSetCookie(request, response, cookieName, "", -1, false);
    }


    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage cookie生效的最大秒数
     * @param isEncode
     * @Description: 设置Cookie的值，并使其在指定时间内生效
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8);
            }
            setCookieToResponse(request, response, cookieName, cookieValue, cookieMaxage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage cookie生效的最大秒数
     * @param encodeString
     * @Description: 设置Cookie的值，并使其在指定时间内生效
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            setCookieToResponse(request, response, cookieName, cookieValue, cookieMaxage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setCookieToResponse(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (cookieMaxage > 0) {
            cookie.setMaxAge(cookieMaxage);
        }
        // 设置域名的cookie
        if (null != request) {
            String domainName = getDomainName(request);
            logger.info("========== domainName: {} ==========", domainName);
            if (!"localhost".equals(domainName)) {
                cookie.setDomain(domainName);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * @return
     * @Description: 得到cookie的域名
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName;

        String serverName = request.getRequestURL().toString();
        if (StringUtils.isEmpty(serverName)) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            if (serverName.indexOf(":") > 0) {
                String[] ary = serverName.split("\\:");
                serverName = ary[0];
            }

            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3 && !isIp(serverName)) {
                // www.xxx.com.cn
                // 根据最新的标准，cookie domain前面不需要加. 比如.xxx.com.cn应该写成xxx.com.cn,这样在aa.xxx.com.cn cookie也是可读取的
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        return domainName;
    }

    private static String trimSpaces(String IP) {//去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    private static boolean isIp(String IP) {//判断是否是一个IP
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String[] split = IP.split("\\.");
            if (Integer.parseInt(split[0]) < 255) {
                if (Integer.parseInt(split[1]) < 255) {
                    if (Integer.parseInt(split[2]) < 255) {
                        if (Integer.parseInt(split[3]) < 255) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }

}
