package com.magic.longvideo.service.user;

import com.magic.longvideo.pojo.dto.LongVideoPaymentOrderRecordDto;
import com.magic.longvideo.pojo.dto.RechargeRecordDto;

import java.util.List;

public interface UserRecordService {
    List<LongVideoPaymentOrderRecordDto> getRechargeRecord(Long userId);

    List<RechargeRecordDto> getCoinDetails(Long userId);
}
