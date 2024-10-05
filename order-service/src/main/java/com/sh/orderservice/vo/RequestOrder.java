package com.sh.orderservice.vo;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RequestOrder {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
