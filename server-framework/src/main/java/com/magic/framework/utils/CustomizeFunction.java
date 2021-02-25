package com.magic.framework.utils;

public interface CustomizeFunction<S, T> {

    /**
     * 用于List转换时，能够自定义转换方式，或者指定参数名
     * @param source
     * @param target
     */
    void customize(S source, T target);
}
