package com.magic.framework.structs;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class JsonResult<T> {
    private long code;
    private String msg;
    private T data;


    private JsonResult(long c, String m, T d) {
        code = c;
        msg = m;
        data = d;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long c) {
        code = c;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String m) {
        msg = m;
    }

    public T getData() {
        return data;
    }

    public void setData(T d) {
        data = d;
    }

    public static <T> JsonResult<T> result(String msg) {
        return new JsonResult<>(ResultCode.RESULT.getCode(), msg, null);
    }

    public static <T> JsonResult<T> success() {
        return new JsonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    public static <T> JsonResult<T> success(T d) {
        return new JsonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), d);
    }

    public static <T> JsonResult<T> success(String msg) {
        return new JsonResult<>(ResultCode.SUCCESS.getCode(), msg, null);
    }

    public static <T> JsonResult<T> success(String msg, T d) {
        return new JsonResult<>(ResultCode.SUCCESS.getCode(), msg, d);
    }

    public static <T> JsonResult<T> successMap(String key, T d) {
        Map<String, T> map = new HashMap<>(1);
        map.put(key, d);
        return new JsonResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), map);
    }

    public static <T> JsonResult<T> failed() {
        return new JsonResult<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg(), null);
    }

    public static <T> JsonResult<T> failed(String msg) {
        return new JsonResult<>(ResultCode.FAILED.getCode(), msg, null);
    }

    public static <T> JsonResult<T> failed(String msg, T data) {
        return new JsonResult<>(ResultCode.FAILED.getCode(), msg, data);
    }
}
