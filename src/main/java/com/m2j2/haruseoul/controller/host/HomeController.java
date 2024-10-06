package com.m2j2.haruseoul.controller.host;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("hostHomeController")
@RequestMapping("host")
public class HomeController {

    @GetMapping("home")
    public String home() {
        return "host/home";
    }

    @GetMapping("login")
    public String login() {
        return "host/login";
    }

}

