package ca.babpool.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class RequestParamEndpointMapping {
    private static final Map<Pattern, RequestParamEndpoint> patternMap = createPatternMap();

    public static Map<Pattern, RequestParamEndpoint> createPatternMap() {
        Map<Pattern, RequestParamEndpoint> map = new HashMap<>();
        map.put(Pattern.compile("^/api/v1/restaurant/(\\d+)$"), RequestParamEndpoint.RESTAURANT);
        map.put(Pattern.compile("^/api/v1/restaurant/(\\d+)/status$"), RequestParamEndpoint.RESTAURANT_STATUS);
        map.put(Pattern.compile("^/api/v1/restaurant/(\\d+)/newOrder$"), RequestParamEndpoint.RESTAURANT_NEW_ORDER);
        map.put(Pattern.compile("^/api/v1/restaurant/(\\d+)/changeStatus$"), RequestParamEndpoint.RESTAURANT_CHANGE_STATUS);
        map.put(Pattern.compile("^/api/v1/restaurant/(\\d+)/refund$"), RequestParamEndpoint.RESTAURANT_REFUND);
        map.put(Pattern.compile("^/api/v1/menu/(\\d+)$"), RequestParamEndpoint.MENU);
        map.put(Pattern.compile("^/api/v1/menu/(\\d+)/representative$"), RequestParamEndpoint.MENU_REPRESENTATIVE);
        map.put(Pattern.compile("^/api/v1/menu/(\\d+)/soldOutHide$"), RequestParamEndpoint.MENU_SOLD_OUT_HIDE);
        map.put(Pattern.compile("^/api/v1/menu/(\\d+)/menuOption$"), RequestParamEndpoint.MENU_MENU_OPTION);
        map.put(Pattern.compile("^/api/v1/review/owner/(\\d+)/(\\d+)$"), RequestParamEndpoint.REVIEW_OWNER_COMMENT_DELETE);
        map.put(Pattern.compile("^/api/v1/statistics/(\\d+)$"), RequestParamEndpoint.STATISTICS);
        map.put(Pattern.compile("^/api/v1/orderDetails/(\\d+)$"), RequestParamEndpoint.ORDER_DETAILS);
        map.put(Pattern.compile("^/api/v1/orderDetails$"), RequestParamEndpoint.ORDER_DETAILS_NO_ID);
        return map;
    }

    public static RequestParamEndpoint findEndpoint(String requestURI) {
        for (Map.Entry<Pattern, RequestParamEndpoint> entry : patternMap.entrySet()) {
            Pattern pattern = entry.getKey();
            if (pattern.matcher(requestURI).matches()) {
                return entry.getValue();
            }
        }
        return null;
    }
}
