package com.yetx.constant;

public interface OrderStatus {
//  订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
    int NO_PAY = 0;
    int HAVE_PAY = 1;
    int HAVE_SEND = 2;
    int HAVA_RECEIVED = 3;
    int HAVE_REFUND = 4;
    int DONE = 5;
}
