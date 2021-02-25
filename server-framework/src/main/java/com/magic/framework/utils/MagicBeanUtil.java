package com.magic.framework.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MagicBeanUtil extends cn.hutool.core.bean.BeanUtil {

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, CustomizeFunction<S, T> function, String... ignores) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t, ignores);
            if (function != null) {
                function.customize(source, t);
            }
            list.add(t);
        }
        return list;
    }
}
