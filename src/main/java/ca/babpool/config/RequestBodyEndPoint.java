package ca.babpool.config;

public enum RequestBodyEndPoint {
    REVIEW_OWNER_NEW_COUPON("/api/v1/review/owner/newCoupon"),
    REVIEW_OWNER_WRITE("/api/v1/review/owner"),
    RESTAURANT_CHANGE_ORDER_STATUS("/api/v1/restaurant/changeStatus"),
    RESTAURANT_ORDER_SEARCH("/api/v1/orderDetails"),
    MENU_UPDATE("/api/v1/menu/update"),
    MENU_ADD("/api/v1/menu/add"),
    MENU_REPRESENTATIVE("/api/v1/menu/representative"),
    MENU_DELETE("/api/v1/menu/deleteMenu"),
    MENU_OPTION_ADD("/api/v1/menu/menuOption/add"),
    MENU_OPTION_UPDATE("/api/v1/menu/menuOption/update");

    private final String endpoint;

    RequestBodyEndPoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public static RequestBodyEndPoint findByEndpoint(String requestURI) {
        for (RequestBodyEndPoint endPoint : values()) {
            if (endPoint.getEndpoint().equals(requestURI)) {
                return endPoint;
            }
        }
        return null;
    }
}
