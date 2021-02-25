package com.magic.framework.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class MagicDateUtil extends DateUtil {
    public static long toLongSecond(Date date) {
        return Long.parseLong(DateUtil.format(date, "yyMMddHHmm"));
    }
}
