package com.magic.framework.utils;

public class StackTraceUtil {
    public static String getStackTraceString(StackTraceElement[] stackTraceElements) {
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement item : stackTraceElements) {
            if (item.isNativeMethod()) {
                continue;
            }
            String className = item.getClassName();
            if (className.startsWith("org.") ||
                    className.startsWith("sun.") ||
                    className.startsWith("java.") ||
                    className.startsWith("javax.")) {
                continue;
            }
            sb.append(String.format("at %s.%s(%s:%d)\n", item.getClassName(), item.getMethodName(), item.getFileName(), item.getLineNumber()));
            //if (item.getClassName().endsWith("Controller")) break;
        }
        return sb.toString();
    }
}
