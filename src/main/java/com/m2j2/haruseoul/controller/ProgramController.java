package com.m2j2.haruseoul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("programController")
@RequestMapping("program")
public class ProgramController {

    @GetMapping("detail")
    public String detail(){
        return "program/detail";
    }

    @GetMapping("list")
    public String list(){
        return "program/list";
    }

    @GetMapping("pay")
    public String pay(){
        return "program/pay";
    }
}
