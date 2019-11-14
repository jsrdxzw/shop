package com.jsrdxzw.utils;

/**
 * 通用脱敏工具类
 * 可用于：
 * 用户名
 * 手机号
 * 邮箱
 * 地址等
 *
 * @author xuzhiwei
 */
public class DesensitizationUtil {

    private static final int SIZE = 6;
    private static final String SYMBOL = "*";

    public static void main(String[] args) {
        String name = commonDisplay("慕课网");
        String mobile = commonDisplay("13900000000");
        String mail = commonDisplay("admin@imooc.com");
        String address = commonDisplay("北京大运河东路888号");

        System.out.println(name);
        System.out.println(mobile);
        System.out.println(mail);
        System.out.println(address);
    }

    /**
     * 通用脱敏方法
     *
     * @param value
     * @return
     */
    public static String commonDisplay(String value) {
        if (null == value || "".equals(value)) {
            return value;
        }
        int len = value.length();
        int pamaone = len / 2;
        int pamatwo = pamaone - 1;
        int pamathree = len % 2;
        StringBuilder stringBuilder = new StringBuilder();
        if (len <= 2) {
            if (pamathree == 1) {
                return SYMBOL;
            }
            stringBuilder.append(SYMBOL);
            stringBuilder.append(value.charAt(len - 1));
        } else {
            if (pamatwo <= 0) {
                stringBuilder.append(value, 0, 1);
                stringBuilder.append(SYMBOL);
                stringBuilder.append(value, len - 1, len);

            } else if (pamatwo >= SIZE / 2 && SIZE + 1 != len) {
                int pamafive = (len - SIZE) / 2;
                stringBuilder.append(value, 0, pamafive);
                stringBuilder.append(SYMBOL.repeat(SIZE));
                stringBuilder.append(value, len - (pamafive + 1), len);
            } else {
                int pamafour = len - 2;
                stringBuilder.append(value, 0, 1);
                stringBuilder.append(SYMBOL.repeat(pamafour));
                stringBuilder.append(value, len - 1, len);
            }
        }
        return stringBuilder.toString();
    }

}