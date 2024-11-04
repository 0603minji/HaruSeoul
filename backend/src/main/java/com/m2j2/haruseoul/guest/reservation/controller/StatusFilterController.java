package com.m2j2.haruseoul.guest.reservation.controller;

import com.m2j2.haruseoul.entity.Status;
import com.m2j2.haruseoul.guest.reservation.service.DefaultStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("statuses")
public class StatusFilterController {

    DefaultStatusService statusService;

    public StatusFilterController(DefaultStatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    private ResponseEntity<List<Status>> getList() {
        List<Status> statuses = statusService.getList();
        return ResponseEntity.ok(statuses);
    }


}
