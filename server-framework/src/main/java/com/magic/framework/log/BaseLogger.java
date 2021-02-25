package com.magic.framework.log;

public interface BaseLogger {

    void debug();
    void info();
    void warn();
    void error();

    public static void main(String[] args) {
        LogStashLogger LogStashLogger= new LogStashLogger();
        LogStashLogger.debug();
    }
}
