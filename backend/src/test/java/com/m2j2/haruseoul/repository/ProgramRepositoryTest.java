//package com.m2j2.haruseoul.repository;
//
//import com.m2j2.haruseoul.entity.Program;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ProgramRepositoryTest {
//
//    @Autowired
//    private ProgramRepository programRepository;
//
//    @Test
//    public void test() {
//        List<Program> programs = programRepository.findAll();
//        programs.forEach(System.out::println);
//
//    }
//}
