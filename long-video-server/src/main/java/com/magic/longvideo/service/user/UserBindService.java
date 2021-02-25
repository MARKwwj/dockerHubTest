package com.magic.longvideo.service.user;

public interface UserBindService {
    boolean bindCode(Long userId, String code);

    boolean bindPhone(Long userId, String phoneNumber, String verificationCode);
}
