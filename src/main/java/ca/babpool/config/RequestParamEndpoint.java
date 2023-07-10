package ca.babpool.config;

public enum RequestParamEndpoint {
    RESTAURANT("/api/v1/restaurant/{restaurantId}"),
    RESTAURANT_STATUS("/api/v1/restaurant/{restaurantId}/status"),
    RESTAURANT_NEW_ORDER("/api/v1/restaurant/{restaurantId}/newOrder"),
    RESTAURANT_CHANGE_STATUS("/api/v1/restaurant/{restaurantId}/changeStatus"),
    RESTAURANT_REFUND("/api/v1/restaurant/{restaurantId}/refund"),
    MENU("/api/v1/menu/{restaurantId}"),
    MENU_REPRESENTATIVE("/api/v1/menu/{restaurantId}/representative"),
    MENU_SOLD_OUT_HIDE("/api/v1/menu/{restaurantId}/soldOutHide"),
    MENU_DELETE_MENU("/api/v1/menu/{restaurantId}/deleteMenu"),
    MENU_MENU_OPTION("/api/v1/menu/{restaurantId}/menuOption"),
    REVIEW_OWNER_COMMENT_DELETE("/api/v1/review/owner/{restaurantId}/{reviewCommentId}"),
    STATISTICS("/api/v1/statistics/{restaurantId}"),
    ORDER_DETAILS("/api/v1/orderDetails/{restaurantId}"),
    ORDER_DETAILS_NO_ID("/api/v1/orderDetails");

    private final String endpoint;

    RequestParamEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
