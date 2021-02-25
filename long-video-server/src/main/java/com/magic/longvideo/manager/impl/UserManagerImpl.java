package com.magic.longvideo.manager.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.manager.UserManager;
import com.magic.longvideo.mapper.LongVideoUserMapper;
import com.magic.longvideo.pojo.dto.LongVideoUserDto;
import com.magic.longvideo.pojo.entity.LongVideoUserEntity;
import org.springframework.stereotype.Service;

/**
 * @author 仁义礼智信
 */
@Service
public class UserManagerImpl implements UserManager {

    private final LongVideoUserMapper mapper;

    public UserManagerImpl(LongVideoUserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean addCoins(int coins) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("gold_coins=gold_coins+" + coins);
        updateWrapper.eq("user_id", userId);
        return 1 == mapper.update(null, updateWrapper);
    }

    @Override
    public boolean deductCoins(int coins) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("gold_coins=gold_coins-" + coins);
        updateWrapper.eq("user_id", userId);
        updateWrapper.ge("gold_coins", coins);
        return 1 == mapper.update(null, updateWrapper);
    }

    @Override
    public boolean addVipTime(int vipTime) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("vip_end_time = date_add( IF ( ISNULL( vip_end_time ), now(), vip_end_time ), INTERVAL " + vipTime + " SECOND )");
        updateWrapper.eq("user_id", userId);
        return 1 == mapper.update(null, updateWrapper);
    }

    @Override
    public boolean addVipTimeAndCoins(int coins, int vipTime) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("vip_end_time = date_add( IF ( ISNULL( vip_end_time ), now(), vip_end_time ), INTERVAL " + vipTime + " SECOND )");
        updateWrapper.setSql("gold_coins=gold_coins+" + coins);
        updateWrapper.eq("user_id", userId);
        return 1 == mapper.update(null, updateWrapper);
    }

    @Override
    public LongVideoUserDto queryUserInfoById(Long userId) {
        LongVideoUserEntity entity = mapper.selectById(userId);
        return MagicBeanUtil.copyProperties(entity, LongVideoUserDto.class);

    }


}
