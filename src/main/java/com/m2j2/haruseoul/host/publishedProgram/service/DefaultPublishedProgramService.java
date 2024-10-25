package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.entity.Status;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreateDto;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreatedDto;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import com.m2j2.haruseoul.repository.StatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultPublishedProgramService implements PublishedProgramService {

    ProgramRepository programRepository;
    PublishedProgramRepository publishedProgramRepository;
    StatusRepository statusRepository;

    public DefaultPublishedProgramService(ProgramRepository programRepository, 
                                          PublishedProgramRepository publishedProgramRepository,
                                          StatusRepository statusRepository) {
        this.programRepository = programRepository;
        this.publishedProgramRepository = publishedProgramRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto) {
        // <editor-fold desc="입력받은 진행일 유효성 검사코드">
        /* 애초에 프론트 달력ui에서 유효하지 않은 날짜는 선택불가능하도록 처리하겠지만
         혹시라도 입력받은 진행일이 유효하지 않은 경우? 1. 개설가능한 날짜 범위(개설당일+3일 ~ 14일?) 외, 2. 이미 개설된 날짜거나*/

        // 1. 개설가능한 날짜 범위 외
        // 개설가능한 날짜(firstAvailableDate ~ lastAvailableDate)
        LocalDate firstAvailableDate = LocalDate.now().plusDays(3); // 오늘로부터 3일 이후
        LocalDate lastAvailableDate = firstAvailableDate.plusDays(14); // 최초 개설가능일~14일
        List<LocalDate> inValidDates = new ArrayList<>();
        for (LocalDate date : publishedProgramCreateDto.getDates()) {
            if (date.isBefore(firstAvailableDate) || date.isAfter(lastAvailableDate))
                inValidDates.add(date);
            // 예외처리 필요
            if (!inValidDates.isEmpty()) {
                System.out.println("프로그램 개설 실패: 유효하지 않은 날짜.");
                inValidDates.forEach(System.out::println);
                System.out.println("개설가능일: 3일 후부터 2주간");
                return null;
            }
        }

//        // 2. 이미 개설된 날짜. 아래 코드대신 DB published_program date컬럼에 unique제약조건 추가하는걸로 대체함
//        List<PublishedProgram> duplicatePublishedPrograms = publishedProgramRepository.findByDateIn(publishedProgramCreateDto.getDates());
//        if (!duplicatePublishedPrograms.isEmpty()) {
//            // 예외처리 필요
//            System.out.println("프로그램 개설 실패: 해당 일자에 이미 개설된 프로그램이 존재합니다.");
//            duplicatePublishedPrograms.stream()
//                    .map(PublishedProgram::getDate)
//                    .forEach(System.out::println);
//            return null;
//        }
        // </editor-fold>

        // ** PublishedProgram엔티티에 필드로 등록될 값들
        // 개설할 프로그램 entity
        Program program = programRepository.findById(publishedProgramCreateDto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with ID: " + publishedProgramCreateDto.getProgramId()));
        // 개설된 프로그램의 status 초기값
        Long initialStatusId = 1L;
        Status initialStatus = statusRepository.findById(initialStatusId)
                .orElseThrow(() -> new EntityNotFoundException("Status not found with ID: " + initialStatusId));


        // 개설할 프로그램 리스트 만들어서 saveAll(개설된 프로그램 리스트)
        List<PublishedProgram> publishedPrograms = new ArrayList<>();
        for (LocalDate date : publishedProgramCreateDto.getDates()) {
            PublishedProgram publishedProgram = PublishedProgram.builder()
                    .program(program)
                    .date(date)
                    .groupSizeCurrent(0) // 초기값 0
                    .status(initialStatus)
                    .build();
            publishedPrograms.add(publishedProgram);
        }
        List<PublishedProgram> savedPublishedPrograms = publishedProgramRepository.saveAll(publishedPrograms);
        return PublishedProgramCreatedDto.builder()
                .regMemberId(publishedProgramCreateDto.getRegMemberId())
                .programId(publishedProgramCreateDto.getProgramId())
                .build();
    }
}