package com.m2j2.haruseoul.controller.host;

import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.entity.ProgramView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

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
