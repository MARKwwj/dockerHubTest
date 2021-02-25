package com.magic.framework.utils;

import cn.hutool.core.lang.UUID;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.magic.framework.redis.JedisUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;


public class TokenUtil {

    private static final String TOKEN_FIELD = "X-Token";

    private static final String SALT = "123456";

    private static final String USER_ID = "userId";

    private static final String APP_ID = "appId";

    private static final int EXPIRE_SECOND = 216000;

    private static final String LOGIN_USER_INFO = "LOGIN_USER_INFO_";

    public static Long getUserIdByTokenFromHeader() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = httpServletRequest.getHeader(TOKEN_FIELD);
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(USER_ID).asLong();
    }

    public static Integer getAppIdByTokenFromHeader() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = httpServletRequest.getHeader(TOKEN_FIELD);
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(APP_ID).asInt();
    }

    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SALT))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Long userId = decodedJWT.getClaim(USER_ID).asLong();
            Long appId = decodedJWT.getClaim(APP_ID).asLong();
            return token.equals(JedisUtil.getStr(LOGIN_USER_INFO + userId + "_" + appId));
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 根据给定字段创建token并保存至Redis
     *
     * @param userId 用户ID
     * @param appId  AppID
     * @return token
     */
    public static String generateTokenAndSaveRedis(Long userId, Integer appId) {
        try {
            if (userId == null) {
                return null;
            }
            String token = JWT.create()
                    .withClaim(USER_ID, userId)
                    .withClaim(APP_ID, appId)
                    .withJWTId(UUID.fastUUID().toString())
                    .sign(Algorithm.HMAC256(SALT));
            JedisUtil.setStr(LOGIN_USER_INFO + userId + "_" + appId, token, EXPIRE_SECOND);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getUserIdWithToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SALT))
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim(USER_ID).asLong();
    }

}

final class RequestBodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public RequestBodyReaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }
}
