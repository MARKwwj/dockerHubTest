package com.magic.longvideo.manager;

import com.magic.longvideo.pojo.dto.LongVideoUserDto;

public interface UserManager {

    boolean addCoins(int coins);

    boolean deductCoins(int coins);

    boolean addVipTime(int vipTime);

    boolean addVipTimeAndCoins(int coins, int vipTime);

    LongVideoUserDto queryUserInfoById(Long userId);
}
