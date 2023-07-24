package ca.babpool.service;

import ca.babpool.model.dto.restaurant.RestaurantNewOrderDto;

import java.sql.SQLException;

public interface OrderService {

    int insertNewOrder(RestaurantNewOrderDto dto) throws SQLException;
}
