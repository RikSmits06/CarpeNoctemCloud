package org.carpenoctemcloud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the homepage located at "/". This is where the user should start looking.
 */
@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("")
public class HomePageController {

    private HomePageController() {
    }

    /**
     * Mapping for the home page html file.
     *
     * @param model The model of the page, is not used for this one.
     * @return String to the template of the homePage.
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping({"", "/"})
    public String getHomePage(Model model) {
        return "homePage";
    }
}
