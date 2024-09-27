package com.m2j2.haruseoul.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("guestHomeController")
@RequestMapping("guest")
public class HomeController {

    @GetMapping("login")
    public String login() {
        return "guest/login";
    }
}
