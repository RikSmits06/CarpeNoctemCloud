package org.carpenoctemcloud.request_logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor used to log which endpoints are mainly being targeted.
 */
@SuppressWarnings("NullableProblems")
@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private final RequestLogService requestLogService;

    /**
     * Creates a new RequestLogInterceptor with the required services.
     *
     * @param requestLogService The log service used to add logs of the request.
     */
    public RequestLogInterceptor(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) {
        // We are only logging successful attempts as this gives the best view of the user flow.
        if (HttpStatusCode.valueOf(response.getStatus()).isError()) {
            return;
        }

        requestLogService.incrementRequestCounter(request.getRequestURI());
    }
}
