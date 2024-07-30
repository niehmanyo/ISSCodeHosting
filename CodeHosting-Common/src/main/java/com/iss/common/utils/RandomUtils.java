package com.iss.common.utils;

import java.security.SecureRandom;

public class RandomUtils {

    // 定义验证码字符集
    private static final String NUMERIC_CHARACTERS = "0123456789";
    private static final String ALPHA_NUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // SecureRandom 实例
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成指定长度的数字验证码
     * @param length 验证码长度
     * @return 生成的数字验证码
     */
    public static String generateNumericCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(NUMERIC_CHARACTERS.length());
            sb.append(NUMERIC_CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的字母数字验证码
     * @param length 验证码长度
     * @return 生成的字母数字验证码
     */
    public static String generateAlphaNumericCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHA_NUMERIC_CHARACTERS.length());
            sb.append(ALPHA_NUMERIC_CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
