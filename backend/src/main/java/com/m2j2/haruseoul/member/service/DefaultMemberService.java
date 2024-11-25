package com.m2j2.haruseoul.member.service;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.member.dto.MemberCreateDto;
import com.m2j2.haruseoul.member.dto.MemberListDto;
import com.m2j2.haruseoul.member.dto.MemberUpdateDto;
import com.m2j2.haruseoul.auth.dto.SigninDto;
import com.m2j2.haruseoul.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DefaultMemberService implements MemberService {

    final MemberRepository memberRepository;
    final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public DefaultMemberService(MemberRepository memberRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Member save(MemberCreateDto memberCreateDto) {

        Member member = Member.builder()
                .name(memberCreateDto.getName())
                .userId(memberCreateDto.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(memberCreateDto.getUserPwd()))
                .nickname(memberCreateDto.getNickname())
                .email(memberCreateDto.getEmail())
                .birth(memberCreateDto.getBirth())
                .build();

        return memberRepository.save(member);
    }

    @Override
    public String validateId(String userId) {
        if (memberRepository.existsByUserId(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT); // 중복된 경우 409 에러 전송
        }
        return "아이디확인필요";
    }


    @Override
    public MemberListDto getList(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return modelMapper.map(member, MemberListDto.class);
    }


    @Override
    @Transactional
    public void update(MemberUpdateDto memberUpdateDto,MultipartFile file) {

        Member member = memberRepository.findById(memberUpdateDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));


        if(file != null) {
            MultipartFile profileImg = file;
            String fileName = UUID.randomUUID() + "_" + profileImg.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, profileImg.getBytes());

                member.setProfileImgSrc("uploads/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        if(memberUpdateDto.getNickname() != null) {
        member.setNickname(memberUpdateDto.getNickname());

        }
        if(memberUpdateDto.getNewPwd() != null) {

        member.setUserPwd(memberUpdateDto.getNewPwd());
        }
        memberRepository.save(member);
    }

    @Override
    public Member validatePwd(Long memberId, String currentPwd) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if (!currentPwd.equals(member.getUserPwd())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }


    @Override
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        member.setStatus("deleted");
        memberRepository.save(member);
    }

}
