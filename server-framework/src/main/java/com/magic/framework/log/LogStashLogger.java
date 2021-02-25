package com.magic.framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogStashLogger implements BaseLogger{

    public Logger builder(Class<?> clazz){
        return LoggerFactory.getLogger(clazz);
    }

    @Override
    public void debug() {
    }

    @Override
    public void info() {

    }

    @Override
    public void warn() {

    }

    @Override
    public void error() {

    }
}
