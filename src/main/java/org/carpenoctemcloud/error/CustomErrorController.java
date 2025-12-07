package org.carpenoctemcloud.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object statusObj  = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object messageObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object pathObj    = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        Integer statusCode = statusObj != null ? Integer.valueOf(statusObj.toString()) : 500;

        model.addAttribute("status", statusCode);
        model.addAttribute("message", messageObj);
        model.addAttribute("path", pathObj);

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        }
        if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        }
        if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error/500";
        }

        return "error/500";
    }
}
