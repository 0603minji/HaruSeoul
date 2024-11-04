package com.m2j2.haruseoul.anonymous.controller;

import com.m2j2.haruseoul.anonymous.dto.TransportationDto;
import com.m2j2.haruseoul.anonymous.service.DefaultTransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("transportation")
public class TransportationController {
    private final DefaultTransportationService service;

    @GetMapping
    public ResponseEntity<List<TransportationDto>> getList() {
        List<TransportationDto> transportationDto = service.getList();
        return ResponseEntity.ok(transportationDto);
    }
}
