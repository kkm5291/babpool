package ca.babpool.service;

import ca.babpool.mapper.*;
import ca.babpool.model.dto.orderdetails.OrderDetailsDto;
import ca.babpool.model.dto.orders.*;
import ca.babpool.model.dto.restaurant.RestaurantNewOrderDto;
import ca.babpool.model.entity.OrderMenu;
import ca.babpool.model.entity.OrderMenuSub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrdersMapper ordersMapper;
    private final OrderDetailsMapper orderDetailsMapper;
    private final OrderMenuMapper orderMenuMapper;
    private final OrderMenuSubMapper orderMenuSubMapper;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public int insertNewOrder(RestaurantNewOrderDto dto) {
        couponMapper.deleteCoupon(dto.getCouponId());
        Long ordersId = addOrders(dto.getOrdersDto());
        addOrderMenuItems(dto.getOrderMenuRequestDtoList(), ordersId);
        return addOrderDetails(dto.getOrderDetailsDto(), ordersId);
    }

    private void addOrderMenuSubItems(List<OrderMenuSubDto> orderMenuSubDtoList, Long orderMenuId) {
        for (OrderMenuSubDto orderMenuSubDto : orderMenuSubDtoList) {
            orderMenuSubDto.setOrderMenuId(orderMenuId);
            orderMenuSubMapper.addOrderMenuSub(orderMenuSubDto);
        }
    }

    private void addOrderMenuItems(List<OrderMenuRequestDto> orderMenuRequestDtoList, Long ordersId) {
        for (OrderMenuRequestDto orderMenuRequestDto : orderMenuRequestDtoList) {
            Long orderMenuId = addOrderMenu(orderMenuRequestDto, ordersId);
            addOrderMenuSubItems(orderMenuRequestDto.getOrderMenuSubDtoList(), orderMenuId);
        }
    }

    private Long addOrderMenu(OrderMenuRequestDto orderMenuRequestDto, Long ordersId) {
        orderMenuRequestDto.setOrdersId(ordersId);
        orderMenuMapper.addOrderMenu(orderMenuRequestDto);
        return orderMenuRequestDto.getOrderMenuId();
    }

    private int addOrderDetails(OrderDetailsDto orderDetailsDto, Long ordersId) {
        orderDetailsDto.setOrdersId(ordersId);
        return orderDetailsMapper.addOrderDetails(orderDetailsDto);
    }

    private Long addOrders(OrdersDto ordersDto) {
        ordersMapper.addOrders(ordersDto);
        Long ordersId = ordersDto.getOrdersId();
        return ordersId;
    }
}
