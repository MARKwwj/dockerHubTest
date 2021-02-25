package com.magic.task.consumer;

public abstract class BaseTaskHandler {
    public abstract String getTaskType();
    public abstract String getTaskSummary();
    public abstract String getTaskDataStruct();
    public abstract void exec(String data);
}
