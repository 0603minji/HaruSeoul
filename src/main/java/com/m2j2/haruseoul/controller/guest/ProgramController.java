package com.m2j2.haruseoul.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("guestProgramController")
@RequestMapping("guest/program")
public class ProgramController {

    @GetMapping("detail")
    public String detail(){
        return "guest/program/detail";
    }

    @GetMapping("list")
    public String list(){
        return "guest/program/list";
    }

    @GetMapping("pay")
    public String pay(){
        return "guest/program/pay";
    }
}
