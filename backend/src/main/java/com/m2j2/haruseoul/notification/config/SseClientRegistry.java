package com.m2j2.haruseoul.notification.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseClientRegistry {

    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();

    public void addClient(Long userId, SseEmitter emitter) {
        clients.put(userId, emitter);
    }

    public SseEmitter getClient(Long userId) {
        return clients.get(userId);
    }

    public void removeClient(Long userId) {
        clients.remove(userId);
    }
}
