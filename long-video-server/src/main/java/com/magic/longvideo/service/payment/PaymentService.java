package com.magic.longvideo.service.payment;

import com.magic.longvideo.pojo.dto.LongVideoPaymentChannelDto;

import java.util.List;

public interface PaymentService {
    List<LongVideoPaymentChannelDto> payMethod(int commodityType);

}
