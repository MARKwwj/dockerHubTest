package com.magic.gateway.handlers;

import com.magic.gateway.utils.ApiUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
    @Bean("aes")
    public GatewayDataHandler aesHandler() {
        return (data, encrypt) -> {
            if (encrypt) {
                return ApiUtil.aesEncrypt(data);
            }
            return ApiUtil.aesDecrypt(data);
        };
    }

    @Bean("zlib")
    public GatewayDataHandler zlibHandler() {
        return (data, encrypt) -> {
            if (encrypt) {
                return ApiUtil.zlibCompressBase64(data);
            }
            return ApiUtil.zlibUncompressBase64(data);
        };
    }

    @Bean("zlib-aes")
    public GatewayDataHandler zlibAesHandler() {
        return (data, encrypt) -> {
            byte[] bytes;
            if (encrypt) {
                bytes = ApiUtil.zlibCompress(data);
                return ApiUtil.aesEncrypt(bytes);
            }
            bytes = ApiUtil.aesDecryptToBytes(data);
            return ApiUtil.zlibUncompress(bytes);
        };
    }

    @Bean("zip")
    public GatewayDataHandler zipHandler() {
        return (data, encrypt) -> {
            if (encrypt) {
                byte[] bytes = ApiUtil.zipCompress(data);
                return new String(bytes);
            }
            return ApiUtil.zipUncompress(data.getBytes());
        };
    }

    @Bean("zip-aes")
    public GatewayDataHandler zipAesHandler() {
        return (data, encrypt) -> {
            byte[] bytes;
            if (encrypt) {
                bytes = ApiUtil.zipCompress(data);
                return ApiUtil.aesEncrypt(bytes);
            }
            bytes = ApiUtil.aesDecryptToBytes(data);
            return ApiUtil.zipUncompress(bytes);
        };
    }
}
