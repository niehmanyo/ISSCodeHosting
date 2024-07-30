package com.iss.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    // 普通邮箱的正则表达式
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // NUS 邮箱的正则表达式
    private static final String NUS_EMAIL_REGEX = "^e\\d{7}@u\\.nus\\.edu$";
    private static final Pattern NUS_EMAIL_PATTERN = Pattern.compile(NUS_EMAIL_REGEX);

    /**
     * 判断给定的邮箱地址是否是有效的邮箱地址
     * @param email 要验证的邮箱地址
     * @return 如果是有效的邮箱地址则返回 true，否则返回 false
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * 判断给定的邮箱地址是否是 NUS 邮箱地址
     * @param email 要验证的邮箱地址
     * @return 如果是 NUS 邮箱地址则返回 true，否则返回 false
     */
    public static boolean isNusEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = NUS_EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
