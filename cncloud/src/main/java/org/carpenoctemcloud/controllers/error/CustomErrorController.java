package org.carpenoctemcloud.controllers.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Produces pages which shows that an error occured to an user.
 */
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {
    private CustomErrorController() {
    }

    /**
     * Returns an error page depending on how it was referenced.
     *
     * @param request The request method, contains info about the error code and message.
     * @param model   The model which is used to construct a template.
     * @return A template with the error info.
     */
    @GetMapping({"", "/"})
    public String handleError(HttpServletRequest request, Model model) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object messageObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int status = 500;
        String message = "Could not identify problem, please create an issue on github if this keeps occuring.";

        if (statusObj != null && messageObj != null) {
            status = (int) statusObj;
            message = (String) messageObj;
        }

        model.addAttribute("status", status);
        model.addAttribute("message", message);

        return "error";
    }
}
