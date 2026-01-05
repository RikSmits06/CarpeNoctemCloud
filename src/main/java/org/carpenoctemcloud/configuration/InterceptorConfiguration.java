package org.carpenoctemcloud.configuration;

import org.carpenoctemcloud.auth.AuthInterceptor;
import org.carpenoctemcloud.request_logging.RequestLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures the interceptors of the application.
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private final RequestLogInterceptor requestLogInterceptor;
    private final AuthInterceptor authInterceptor;

    /**
     * Creates a new InterceptorConfiguration.
     *
     * @param requestLogInterceptor The interceptor used for collecting request statistics.
     * @param authInterceptor       The interceptor to look for the logged in user.
     */
    public InterceptorConfiguration(RequestLogInterceptor requestLogInterceptor,
                                    AuthInterceptor authInterceptor) {
        this.requestLogInterceptor = requestLogInterceptor;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor);
        registry.addInterceptor(authInterceptor);
    }
}
