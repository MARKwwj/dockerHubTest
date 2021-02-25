package com.magic.longvideo.service.user.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.magic.framework.utils.MagicUtil;
import com.magic.framework.utils.TokenUtil;
import com.magic.longvideo.mapper.LongVideoUserMapper;
import com.magic.longvideo.pojo.entity.LongVideoUserEntity;
import com.magic.longvideo.pojo.vo.RetrieveAccountByPhoneNumberVo;
import com.magic.longvideo.pojo.vo.RetrieveAccountByQrCodeVo;
import com.magic.longvideo.service.user.UserSafeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSafeServiceImpl implements UserSafeService {

    private final LongVideoUserMapper longVideoUserMapper;

    public UserSafeServiceImpl(LongVideoUserMapper longVideoUserMapper) {
        this.longVideoUserMapper = longVideoUserMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean retrieveAccountByQrCode(RetrieveAccountByQrCodeVo retrieveAccountByQrCodeVo, Long userId) {
        Long oldTokenUserId = TokenUtil.getUserIdWithToken(retrieveAccountByQrCodeVo.getOldToken());
        String oldMachineCode = retrieveAccountByQrCodeVo.getOldMachineCode();
        UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper();
        int deleteRows = longVideoUserMapper.deleteById(userId);
        updateWrapper.eq("machine_code", oldMachineCode);
        updateWrapper.eq("user_id", oldTokenUserId);
        updateWrapper.set("machine_code", retrieveAccountByQrCodeVo.getNewMachineCode());
        int updateRows = longVideoUserMapper.update(null, updateWrapper);
        if (updateRows != 1 || deleteRows != 1) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject retrieveAccountByPhoneNumber(RetrieveAccountByPhoneNumberVo retrieveAccountByPhoneNumberVo, Long userId) {
        if (MagicUtil.checkVerificationCode(retrieveAccountByPhoneNumberVo.getPhoneNumber(), retrieveAccountByPhoneNumberVo.getVerificationCode())) {
            int deleteRows = longVideoUserMapper.deleteById(userId);
            UpdateWrapper<LongVideoUserEntity> updateWrapper = new UpdateWrapper();
            updateWrapper.eq("phone_number", retrieveAccountByPhoneNumberVo.getPhoneNumber());
            updateWrapper.ne("user_id", userId);
            updateWrapper.set("machine_code", retrieveAccountByPhoneNumberVo.getMachineCode());
            int updateRows = longVideoUserMapper.update(null, updateWrapper);
            if (updateRows != 1 || deleteRows != 1) {
                return new JSONObject().set("isSuccess", false).set("message", "旧帐号不存在");
            }
            return new JSONObject().set("isSuccess", true).set("message", "帐号找回成功");
        }
        return new JSONObject().set("isSuccess", false).set("message", "验证码错误");
    }
}
