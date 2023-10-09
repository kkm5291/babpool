package ca.babpool.controller;

import ca.babpool.kafka.KafkaProducer;
import ca.babpool.model.dto.restaurant.RestaurantNewOrderDto;
import ca.babpool.model.response.ApiResponse;
import ca.babpool.model.response.CommonResult;
import ca.babpool.service.FCMNotificationService;
import ca.babpool.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    private final ApiResponse apiResponse;
    private final OrderService orderService;
    private final FCMNotificationService fcmNotificationService;
    private final KafkaProducer kafkaProducer;

    @Operation(summary = "레스토랑 신규주문")
    @PostMapping("/new")
    public CommonResult restaurantNewOrder(@RequestBody RestaurantNewOrderDto dto) throws SQLException, IOException {
        int result = orderService.insertNewOrder(dto);
        // kafka 프로듀서
        if (result == 1) {
            this.kafkaProducer.sendMessage(fcmNotificationService.createNotificationDto(dto));
//            fcmNotificationService.sendNotificationByToken(fcmNotificationService.createNotificationDto(dto));
            return apiResponse.getSuccessResult(result);
        } else {
            return apiResponse.getFailResult();
        }
    }
}
