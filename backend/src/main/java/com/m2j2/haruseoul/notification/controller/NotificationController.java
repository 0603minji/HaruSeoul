package com.m2j2.haruseoul.notification.controller;


import com.m2j2.haruseoul.notification.config.SseClientRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("notifications")
public class NotificationController {

    private final SseClientRegistry sseClientRegistry;

    public NotificationController(SseClientRegistry sseClientRegistry) {
        this.sseClientRegistry = sseClientRegistry;
    }

    @GetMapping("{id}")
    public SseEmitter notifications(@PathVariable Long id) {
        SseEmitter emitter = new SseEmitter(0L); // 무한 타임아웃
        sseClientRegistry.addClient(id, emitter); // SseClientRegistry에 클라이언트 등록

        emitter.onCompletion(() -> sseClientRegistry.removeClient(id));
        emitter.onTimeout(() -> sseClientRegistry.removeClient(id));
        emitter.onError((e) -> sseClientRegistry.removeClient(id));


        return emitter;
    }
}
