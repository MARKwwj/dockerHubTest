package com.magic.task.consumer;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.task.consumer.impls.test.TestTaskHandler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class TaskHandlerManager {
    private Map<String, BaseTaskHandler> handlers = new HashMap<>();
    private Map<String, String> handlerSummaryMap = new HashMap<>();

    @PostConstruct
    private void init() {
        register(new TestTaskHandler());
    }

    private void register(BaseTaskHandler handler) {
        handlers.put(handler.getTaskType(), handler);
    }

    public Map<String, String> getHandlerSummaryMap() {
        return handlerSummaryMap;
    }

    public void exec(String str) {
        JSONObject data = JSONUtil.parseObj(str);
        BaseTaskHandler handler = handlers.get(data.getStr("taskType"));
        if (handler == null) {
            return;
        }

        try {
            handler.exec(data.getStr("taskData"));
        }
        catch (Exception e) {
        }
    }
}
