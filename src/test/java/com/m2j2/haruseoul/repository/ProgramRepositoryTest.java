package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Program;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;

@DataJpaTest
public class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void test() {
        List<Program> programs = programRepository.findAll();
        programs.forEach(System.out::println);

    }
}
