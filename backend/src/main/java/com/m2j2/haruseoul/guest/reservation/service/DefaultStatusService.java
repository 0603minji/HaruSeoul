package com.m2j2.haruseoul.guest.reservation.service;

import com.m2j2.haruseoul.entity.Status;
import com.m2j2.haruseoul.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultStatusService implements StatusService {
    StatusRepository statusRepository;

    public DefaultStatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public List<Status> getList() {
        return statusRepository.findAll();
    }
}
