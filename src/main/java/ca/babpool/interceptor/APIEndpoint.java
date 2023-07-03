package ca.babpool.interceptor;

public enum APIEndpoint {
    RESTAURANT("/api/v1/restaurant/{restaurantId}"),
    RESTAURANT_STATUS("/api/v1/restaurant/{restaurantId}/status"),
    RESTAURANT_NEW_ORDER("/api/v1/restaurant/{restaurantId}/newOrder"),
    RESTAURANT_CHANGE_STATUS("/api/v1/restaurant/{restaurantId}/changeStatus"),
    MENU("/api/v1/menu/{restaurantId}"),
    RESTAURANT_REFUND("/api/v1/restaurant/{restaurantId}/refund"),
    MENU_REPRESENTATIVE("/api/v1/menu/{restaurantId}/representative"),
    MENU_SOLD_OUT_HIDE("/api/v1/menu/{restaurantId}/soldOutHide"),
    MENU_DELETE_MENU("/api/v1/menu/{restaurantId}/deleteMenu"),
    MENU_MENU_OPTION("/api/v1/menu/{restaurantId}/menuOption"),
    REVIEW_OWNER("/api/v1/review/owner/{restaurantId}"),
    REVIEW_OWNER_COMMENT("/api/v1/review/owner/{restaurantId}/{reviewCommentId}"),
    REVIEW_OWNER_NEW_COUPON("/api/v1/review/owner/{restaurantId}/newCoupon"),
    STATISTICS("/api/v1/statistics/{restaurantId}"),
    ORDER_DETAILS("/api/v1/orderDetails/{restaurantId}"),
    ORDER_DETAILS_NO_ID("/api/v1/orderDetails");

    private final String endpoint;

    APIEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
