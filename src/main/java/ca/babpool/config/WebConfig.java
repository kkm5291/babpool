package ca.babpool.config;

import ca.babpool.filter.CopyBodyFilter;
import ca.babpool.interceptor.OwnerRequestBodyInterceptor;
import ca.babpool.interceptor.OwnerRequestParamInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final OwnerRequestParamInterceptor ownerRequestParamInterceptor;
    private final OwnerRequestBodyInterceptor ownerRequestBodyInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:3001","http://172.30.1.50:3000",
                        "http://babpoolme.s3-website.ap-northeast-2.amazonaws.com",
                        "http://babpool-owner.s3-website.ap-northeast-2.amazonaws.com",
                        "https://babpoolapp.shop", "https://babpoolowner.store", "https://www.babpoolme.store"
                        )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*", "Content-Type", "Authorization")
                .allowCredentials(true)
                .maxAge(3000);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ownerRequestParamInterceptor)
                .addPathPatterns("/api/v1/restaurant/{restaurantId:\\d+}/**",
                        "/api/v1/menu/{restaurantId:\\d+}/**",
                        "/api/v1/review/owner/{restaurantId:\\d+}/{reviewCommentId:\\d+}/**",
                        "/api/v1/statistics/{restaurantId:\\d+}/**",
                        "/api/v1/orderDetails/{restaurantId:\\d+}/**");

        registry.addInterceptor(ownerRequestBodyInterceptor)
                .addPathPatterns("/api/v1/orderDetails",
                        "/api/v1/review/owner",
                        "/api/v1/review/owner/newCoupon",
                        "/api/v1/restaurant/changeStatus",
                        "/api/v1/menu/update",
                        "/api/v1/menu/add",
                        "/api/v1/menu/representative",
                        "/api/v1/menu/deleteMenu",
                        "/api/v1/menu/menuOption/add",
                        "/api/v1/menu/menuOption/update"
                );
    }
    @Bean
    public FilterRegistrationBean<CopyBodyFilter> copyBodyFilter() {
        FilterRegistrationBean<CopyBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CopyBodyFilter());
        registrationBean.addUrlPatterns("/api/v1/orderDetails");
        registrationBean.addUrlPatterns("/api/v1/review/owner");
        registrationBean.addUrlPatterns("/api/v1/review/owner/newCoupon");
        registrationBean.addUrlPatterns("/api/v1/restaurant/changeStatus");
        registrationBean.addUrlPatterns("/api/v1/menu/update");
        registrationBean.addUrlPatterns("/api/v1/menu/add");
        registrationBean.addUrlPatterns("/api/v1/menu/representative");
        registrationBean.addUrlPatterns("/api/v1/menu/deleteMenu");
        registrationBean.addUrlPatterns("/api/v1/menu/menuOption/add");
        registrationBean.addUrlPatterns("/api/v1/menu/menuOption/update");
        return registrationBean;
    }

}
