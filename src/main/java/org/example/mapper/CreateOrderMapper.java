package org.example.mapper;

import org.example.dto.OrderDto;
import org.example.entity.Order;

import java.time.LocalDateTime;

public class CreateOrderMapper implements Mapper<OrderDto, Order> {

    private static final CreateOrderMapper INSTANCE = new CreateOrderMapper();

    @Override
    public Order mapFrom(OrderDto object) {
        return Order.builder()
                .customerId(object.getCustomerId())
                .itemInOrders(object.getItemInOrders())
                .createDate(LocalDateTime.now())
                .build();
    }

    public static CreateOrderMapper getInstance() {
        return INSTANCE;
    }
}
