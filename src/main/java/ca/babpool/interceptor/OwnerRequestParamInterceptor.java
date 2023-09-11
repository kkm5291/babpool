package ca.babpool.interceptor;


import ca.babpool.config.RequestParamEndpoint;
import ca.babpool.config.RequestParamEndpointMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OwnerRequestParamInterceptor implements HandlerInterceptor {
    private final InterceptorUtil util;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        String requestURI = request.getRequestURI();
        RequestParamEndpoint requestParamEndpoint = RequestParamEndpointMapping.findEndpoint(requestURI);
        Long requestRestaurantId;

        if (requestParamEndpoint != null) {
            requestRestaurantId = util.extractRestaurantId(requestURI, requestParamEndpoint.getEndpoint());
        } else {
            response.sendRedirect("/api/error/InvalidApiRequestException");
            return false;
        }

        List<Long> loggedInRestaurantId = util.getLoggedInRestaurantIdList(memberId);
        if (!loggedInRestaurantId.contains(requestRestaurantId)) {
            response.sendRedirect("/api/error/AuthenticationException");
            return false;
        }
        return true;
    }
}