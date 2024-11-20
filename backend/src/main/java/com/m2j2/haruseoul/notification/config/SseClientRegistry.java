package com.m2j2.haruseoul.notification.config;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class SseClientRegistry {

    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();

    public void addClient(Long userId, SseEmitter emitter) {
        clients.put(userId, emitter);
        System.out.println("SseEmitter 등록: userId=" + userId);
    }

    public SseEmitter getClient(Long userId) {
        SseEmitter emitter = clients.get(userId);
        System.out.println("SseClientRegistry 상태: " + clients);
        System.out.println("SseEmitter 조회: userId=" + userId + ", emitter=" + emitter);
        return emitter;
    }

    public void removeClient(Long userId) {
        clients.remove(userId);
        System.out.println("SseEmitter 제거: userId=" + userId);
    }

}
