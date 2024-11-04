package com.m2j2.haruseoul.anonymous.service;

import com.m2j2.haruseoul.anonymous.dto.TransportationDto;
import com.m2j2.haruseoul.entity.Transportation;
import com.m2j2.haruseoul.repository.TransportationRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultTransportationService implements TransportationService {
    private final TransportationRepository transportationRepository;

    @Override
    public List<TransportationDto> getList() {
        List<Transportation> transportations = transportationRepository.findAll();

        List<TransportationDto> transportationDtos = transportations
                .stream()
                .map(transportation -> {
                    TransportationDto transportationDto = TransportationDto
                            .builder()
                            .id(transportation.getId())
                            .name(transportation.getName())
                            .build();
                    return transportationDto;
                })
                .toList();

        return transportationDtos;
    }
}
