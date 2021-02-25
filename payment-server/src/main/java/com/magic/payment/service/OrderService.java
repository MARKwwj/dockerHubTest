package com.magic.payment.service;

public interface OrderService {
    String createOrder(Long userId, Integer appType, Integer channelId, Integer commodityId);

    boolean payResult(String orderId);

    String pay(String orderNumber);
}
