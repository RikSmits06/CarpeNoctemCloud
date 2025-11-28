package org.carpenoctemcloud.controllers.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @GetMapping({"/login", "/login/"})
    public ResponseEntity<Void> loginUser() {
        return ResponseEntity.ok().build();
    }
}
