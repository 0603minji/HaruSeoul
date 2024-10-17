package com.m2j2.haruseoul.controller.host;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("hostProgramController")
@RequestMapping("host/program")
public class ProgramController {;

    @GetMapping("create")
    public String create() {
        return "host/program/create";
    }

    @GetMapping("list")
    public String list() {
        return "host/program/list";
    }

}
