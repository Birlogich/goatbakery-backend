package org.example.service;
import org.example.dao.OrderDao;
import org.example.dto.OrderDto;
import org.example.entity.Order;
import org.example.mapper.CreateOrderMapper;

public class OrderService {

    private static final OrderService INSTANCE = new OrderService();
    private final OrderDao orderDao = OrderDao.getInstance();
    private final CreateOrderMapper createOrderMapper = CreateOrderMapper.getInstance();

    private OrderService(){}

    public Integer createOrder(OrderDto createOrderDto) {
        final Order orderEntity = createOrderMapper.mapFrom(createOrderDto);
        orderDao.save(orderEntity);
        return orderEntity.getTotalSum();
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }
}
