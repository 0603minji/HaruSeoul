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


        System.out.println("클라이언트 등록: userId=" + id); // 등록 로그 추가
        System.out.println("현재 클라이언트 상태: " + sseClientRegistry.getClients());

        emitter.onCompletion(() -> {
            sseClientRegistry.removeClient(id);
            System.out.println("클라이언트 연결 종료: userId=" + id);
        });
        emitter.onTimeout(() -> {
            sseClientRegistry.removeClient(id);
            System.out.println("클라이언트 타임아웃: userId=" + id);
        });
        emitter.onError((e) -> {
            sseClientRegistry.removeClient(id);
            System.out.println("클라이언트 연결 오류: userId=" + id + ", 오류: " + e.getMessage());
        });

        return emitter;
    }

}
