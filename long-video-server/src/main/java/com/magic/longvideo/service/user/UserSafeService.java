package com.magic.longvideo.service.user;

import cn.hutool.json.JSONObject;
import com.magic.longvideo.pojo.vo.RetrieveAccountByPhoneNumberVo;
import com.magic.longvideo.pojo.vo.RetrieveAccountByQrCodeVo;

public interface UserSafeService {
    boolean retrieveAccountByQrCode(RetrieveAccountByQrCodeVo machineCode, Long userId);

    JSONObject retrieveAccountByPhoneNumber(RetrieveAccountByPhoneNumberVo retrieveAccountByPhoneNumberVo, Long userId);
}
