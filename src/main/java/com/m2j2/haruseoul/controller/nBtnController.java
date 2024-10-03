package com.m2j2.haruseoul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class nBtnController {

    @GetMapping("/n-btn")
    public String btn() {
        return "n-btn";
    }

}