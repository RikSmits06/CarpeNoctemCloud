package org.carpenoctemcloud.controllers.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {

        int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        model.addAttribute("status", status);
        model.addAttribute("message", message);

        return "error";
    }
}
