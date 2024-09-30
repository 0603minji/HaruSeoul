package com.m2j2.haruseoul.controller.host;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("hostProgramControllerCss")
@RequestMapping("host/program/css")
public class ProgramControllerCss {
    @GetMapping("list")
    public String list(){
        return "host/program/listcss";
    }
}
