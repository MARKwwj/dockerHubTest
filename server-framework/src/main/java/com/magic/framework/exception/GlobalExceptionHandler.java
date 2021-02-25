package com.magic.framework.exception;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String env;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public JsonResult exceptionHandler(Exception e) {
        BufferedReader br = null;
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            br = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
            StringBuilder requestBodyBuilder = new StringBuilder();
            String len;
            while ((len = br.readLine()) != null) {
                requestBodyBuilder.append(len);
                requestBodyBuilder.append("\n");
            }
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().startsWith("com.magic")) {
                    StringBuilder exceptionInfoBuilder = new StringBuilder();
                    if (TokenUtil.verify(httpServletRequest.getHeader("X-Token"))) {
                        exceptionInfoBuilder.append("\n【用户ID】\t");
                        exceptionInfoBuilder.append(TokenUtil.getUserIdByTokenFromHeader());
                    }
                    exceptionInfoBuilder.append("\n【异常类】\t");
                    exceptionInfoBuilder.append(stackTraceElement.getClassName());
                    exceptionInfoBuilder.append("\n【异常信息】\t");
                    exceptionInfoBuilder.append(e.toString());
                    exceptionInfoBuilder.append(" : ");
                    exceptionInfoBuilder.append(e.getMessage());
                    exceptionInfoBuilder.append("\n【异常方法】\t");
                    exceptionInfoBuilder.append(stackTraceElement.getMethodName());
                    exceptionInfoBuilder.append("\n【异常行号】\t");
                    exceptionInfoBuilder.append(stackTraceElement.getLineNumber());
                    if (StrUtil.isNotBlank(requestBodyBuilder)) {
                        exceptionInfoBuilder.append("\n【异常参数】\n");
                        exceptionInfoBuilder.append(requestBodyBuilder);
                    }
                    log.error(exceptionInfoBuilder.toString());
                }
                //TODO 飞机发送
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if ("dev".equals(env)) {
                e.printStackTrace();
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return JsonResult.failed("服务器异常", MapUtil.empty());
    }
}
