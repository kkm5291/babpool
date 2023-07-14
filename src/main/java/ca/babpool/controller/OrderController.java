package ca.babpool.controller;

import ca.babpool.model.dto.restaurant.RestaurantNewOrderDto;
import ca.babpool.model.response.ApiResponse;
import ca.babpool.model.response.CommonResult;
import ca.babpool.service.FCMNotificationService;
import ca.babpool.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final ApiResponse apiResponse;
    private final OrderService orderService;
    private final FCMNotificationService fcmNotificationService;

    @Operation(summary = "레스토랑 신규주문")
    @PostMapping("/new")
    @Transactional
    public CommonResult restaurantNewOrder(@RequestBody RestaurantNewOrderDto dto) {
        int result = orderService.insertNewOrder(dto);
        fcmNotificationService.sendNotificationByToken(fcmNotificationService.createNotificationDto(dto));
        return apiResponse.getSuccessResult(result);
    }
}
