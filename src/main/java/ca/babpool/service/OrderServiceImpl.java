package ca.babpool.service;

import ca.babpool.mapper.*;
import ca.babpool.model.dto.orderdetails.OrderDetailsDto;
import ca.babpool.model.dto.orders.*;
import ca.babpool.model.dto.restaurant.RestaurantNewOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
        OrdersDto ordersDto = dto.getOrdersDto();
        OrderDetailsDto orderDetailsDto = dto.getOrderDetailsDto();
        List<OrderMenuRequestDto> orderMenuRequestDtoList = dto.getOrderMenuRequestDtoList();

        ordersMapper.addOrders(ordersDto);
        Long ordersId = ordersDto.getOrdersId();

        orderDetailsDto.setOrdersId(ordersId);
        orderDetailsMapper.addOrderDetails(orderDetailsDto);

        for (OrderMenuRequestDto orderMenuRequestDto : orderMenuRequestDtoList) {
            orderMenuRequestDto.setOrdersId(ordersId);

            orderMenuMapper.addOrderMenu(orderMenuRequestDto);
            Long orderMenuId = orderMenuRequestDto.getOrderMenuId();
            List<OrderMenuSubDto> orderMenuSubDtoList = orderMenuRequestDto.getOrderMenuSubDtoList();

            for (OrderMenuSubDto orderMenuSubDto : orderMenuSubDtoList) {
                orderMenuSubDto.setOrderMenuId(orderMenuId);
                orderMenuSubMapper.addOrderMenuSub(orderMenuSubDto);
            }
        }

        return couponMapper.deleteCoupon(dto.getCouponId());
    }
}
