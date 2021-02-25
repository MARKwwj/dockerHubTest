package com.magic.framework.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ApiUtil {
    private static final byte[] KEY = "S5WJ3FMDgrCIcJpS".getBytes();
    private static final AES aesInst = SecureUtil.aes(KEY);

    public static String aesEncrypt(byte[] data) {
        return aesInst.encryptBase64(data);
    }

    public static byte[] aesDecryptToBytes(String data) {
        return aesInst.decrypt((data).getBytes());
    }

    public static String aesEncrypt(String data) {
        return aesInst.encryptBase64(data);
    }

    public static String aesDecrypt(String data) {
        return new String(aesInst.decrypt(data));
    }

    public static byte[] zipCompress(String data) {
        if (data == null) {
            return null;
        }
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zos = null;
        try {
            out = new ByteArrayOutputStream();
            zos = new ZipOutputStream(out);
            ZipEntry zipEntry = new ZipEntry("1.json");
            zos.putNextEntry(zipEntry);
            zos.write(data.getBytes());
            out.flush();
            compressed = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            compressed = null;
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
            }
            zos = null;
            try {
                out.close();
            } catch (IOException e) {
            }
            out = null;
        }
        return compressed;
    }

    public static String zipUncompress(byte[] data) {
        byte[] bytes = null;
        ByteArrayInputStream bis = null;
        ZipInputStream zip = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new ByteArrayInputStream(data);
            zip = new ZipInputStream(bis);
            baos = new ByteArrayOutputStream();
            if (zip.getNextEntry() != null) {
                int num = -1;
                byte[] buf = new byte[1024];
                while ((num = zip.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, num);
                }
                baos.flush();
                bytes = baos.toByteArray();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
            }
            bis = null;
            try {
                zip.close();
            } catch (IOException e) {
            }
            zip = null;
            try {
                baos.close();
            } catch (IOException e) {
            }
            baos = null;
        }
        return new String(bytes);
    }

    public static byte[] zlibCompress(String data) {
        return zlibCompress(data, 6);
    }

    public static byte[] zlibCompress(String data, int level) {
        if (StrUtil.isBlank(data)) {
            return null;
        }
        return ZipUtil.zlib(data.getBytes(), level);
    }

    public static String zlibUncompress(byte[] data) {
        return new String(ZipUtil.unZlib(data));
    }

    public static String zlibCompressBase64(String data) {
        return zlibCompressBase64(data, 6);
    }

    public static String zlibCompressBase64(String data, int level) {
        if (StrUtil.isBlank(data)) {
            return null;
        }
        byte[] bytes = ZipUtil.zlib(data.getBytes(), level);
        return Base64.encode(bytes);
    }

    public static String zlibUncompressBase64(String data) {
        if (StrUtil.isBlank(data)) {
            return null;
        }
        byte[] bytes = Base64.decode(data);

        return new String(ZipUtil.unZlib(bytes));
    }

    public static void main(String[] args) {
        int i = 1 / 0;
        try {

        } finally {
            System.out.println("11111");
        }
    }
}
