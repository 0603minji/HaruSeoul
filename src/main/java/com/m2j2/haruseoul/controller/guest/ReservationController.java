package com.m2j2.haruseoul.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("guestReservationController")
@RequestMapping("guest/reservation")
public class ReservationController {

    @GetMapping("list")
    public String list(){
        return "guest/reservation/list-ms";
    }

    @GetMapping("detail")
    public String detail(){
        return "guest/reservation/detail";
    }

}
