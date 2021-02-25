package com.magic.longvideo.service.system;

import cn.hutool.json.JSONObject;

import java.util.List;

public interface ConfigService {
    JSONObject info(List<String> keys);
}
