package com.magic.task.consumer.impls.test;

import com.magic.task.consumer.BaseTaskHandler;
import com.magic.task.consumer.TaskTypes;

public class TestTaskHandler extends BaseTaskHandler {
    @Override
    public String getTaskType() {
        return TaskTypes.TestTask;
    }

    @Override
    public String getTaskSummary() {
        return "测试任务";
    }

    @Override
    public String getTaskDataStruct() {
        return "{test: '', id: ''}";
    }

    @Override
    public void exec(String data) {

    }
}
