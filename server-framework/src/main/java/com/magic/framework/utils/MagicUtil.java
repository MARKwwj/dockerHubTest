package com.magic.framework.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.magic.framework.redis.JedisUtil;

import java.math.BigInteger;
import java.util.Random;

public class MagicUtil {

    public static int hexToInt(String code) {
        BigInteger bigInteger;
        try {
            bigInteger = HexUtil.toBigInteger(code);
        } catch (NumberFormatException e) {
            return 0;
        }
        if (bigInteger == null) {
            return 0;
        }
        return bigInteger.intValue();
    }

    public static String intToHex(int agentId) {
        String code = HexUtil.toHex(agentId);
        return code.toUpperCase();
    }

    private static final String BIND_PHONE_CODE_PREFIX = "BIND_PHONE_CODE_PREFIX_";

    public static boolean checkVerificationCode(String phoneNumber, String verificationCode) {
        verificationCode = verificationCode.toUpperCase();
        StringBuilder keyBuild = new StringBuilder();
        keyBuild.append(BIND_PHONE_CODE_PREFIX);
        keyBuild.append(phoneNumber);
        keyBuild.append("_");
        keyBuild.append(verificationCode);
        if (StrUtil.isNotBlank(JedisUtil.getStr(keyBuild.toString()))) {
            JedisUtil.del(keyBuild.toString());
            return true;
        }
        return false;
    }

    public static String generateVerificationCode(String phoneNumber) {
        StringBuilder keyBuild = new StringBuilder();
        keyBuild.append(BIND_PHONE_CODE_PREFIX);
        keyBuild.append(phoneNumber);
        keyBuild.append("_");
        String verificationCode = generateVerifyCode(6, (long) (Math.random() * 10e15));
        keyBuild.append(verificationCode);
        if (JedisUtil.setStrEx(keyBuild.toString(), phoneNumber, 100000)) {
            return verificationCode;
        }
        return null;
    }

    public static final String VERIFY_CODES = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    public static String generateVerifyCode(int verifySize, long seed) {
        return generateVerifyCode(verifySize, VERIFY_CODES, seed);
    }

    public static String generateVerifyCode(int verifySize, String sources, long seed) {
        if (sources == null || sources.length() == 0) {
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(seed);
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }

    /**
     * 将数字转换成中文 万或亿
     *
     * @param number 需要转换的数字
     * @param point  保留几位小数
     * @return
     */
    public static String numberConvertToStr(Long number, int point) {
        String numStr = number.toString();
        if (numStr.length() < 6) {
            return numStr;
        } else if (numStr.length() > 5 && numStr.length() <= 8) {
            String decimal = numStr.substring(numStr.length() - 4, numStr.length() - 4 + point);
            return Double.parseDouble((number / 10000) + "." + decimal) + "万";
        } else {
            String decimal = numStr.substring(numStr.length() - 8, numStr.length() - 8 + point);
            return Double.parseDouble((number / 100000000) + "." + decimal) + "亿";
        }
    }


    /**
     * 秒转时分秒
     *
     * @param videoDuration
     * @return
     */
    public static String hoursMinuteSecond(Integer videoDuration) {
        int h = videoDuration / 3600;
        int m = (videoDuration % 3600) / 60;
        int s = (videoDuration % 3600) % 60;
        return h + ":" + m + ":" + s;
    }

    /**
     * 时分秒转秒
     *
     * @param hms
     * @return
     */
    private int hmsChangeToSeconds(String hms) {
        String[] hoursMinutesSeconds = hms.split(":");
        int hours = Integer.parseInt(hoursMinutesSeconds[0]);
        int minutes = Integer.parseInt(hoursMinutesSeconds[1]);
        int seconds = Integer.parseInt(hoursMinutesSeconds[2]);
        int duration = hours * 60 * 60 + minutes * 60 + seconds;
        return duration;
    }

}
