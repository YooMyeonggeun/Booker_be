package com.sparta.booker.user.service;

import com.sparta.booker.user.dto.LoginRequestDto;
import com.sparta.booker.user.dto.ResponseDto;
import com.sparta.booker.user.dto.SignupRequestDto;
import com.sparta.booker.user.entity.User;
import com.sparta.booker.user.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Transactional
@Builder
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public ResponseEntity<ResponseDto> signup(SignupRequestDto signupRequestDto){
        String userId = signupRequestDto.getUserId();
        String password = signupRequestDto.getPassword();
        String address = signupRequestDto.getAddress();

        User user = User.builder()
                .userId(userId)
                .password(password)
                .address(address).build();

        userRepository.save(user);
        return ResponseEntity.ok().body(new ResponseDto("회원가입에 성공"));
    }

    // 로그인
    public ResponseEntity<ResponseDto> login(LoginRequestDto loginRequestDto){

        // 입력한 ID를 기반으로 회원 존재 유무 체크
        User user = userRepository.findByUserId(loginRequestDto.getUserId()).orElseThrow(
                () -> new NullPointerException("회원이 존재하지 않습니다.")
        );

        // 비밀번호 일치여부 체크
        if(!user.getPassword().equals(loginRequestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok().body(new ResponseDto("로그인 성공"));
    }
}