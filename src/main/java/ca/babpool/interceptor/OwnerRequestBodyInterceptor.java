package ca.babpool.interceptor;

import ca.babpool.config.RequestBodyEndPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OwnerRequestBodyInterceptor implements HandlerInterceptor {
    private final InterceptorUtil util;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        String requestURI = request.getRequestURI();
        RequestBodyEndPoint requestBodyEndPoint = RequestBodyEndPoint.findByEndpoint(requestURI);
        List<Long> loggedIntRestaurantId = util.getLoggedInRestaurantIdList(memberId);
        Long requestRestaurantId;

        if (requestBodyEndPoint != null) {
            requestRestaurantId = util.parseRequestServletBody(request);
        } else {
            response.sendRedirect("/api/error/InvalidApiRequestException");
            return false;
        }

        if (!loggedIntRestaurantId.contains(requestRestaurantId)) {
            response.sendRedirect("/api/error/AuthenticationException");
            return false;
        }
        return true;
    }
}
