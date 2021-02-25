package com.magic.framework.utils;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class MagicHttpUtil extends HttpUtil {

    public static String postStringMap(String urlString, Map<String, String> paramMap, int timeout) {
        return post(urlString, new HashMap<>(paramMap), timeout);
    }
}
