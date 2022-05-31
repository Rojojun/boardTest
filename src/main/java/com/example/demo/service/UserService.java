package com.example.demo.service;

import com.example.demo.domain.Users;
import com.example.demo.models.SignupRequestDto;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(SignupRequestDto requestDto) {
        //회원 ID 중복 확인
        String username = requestDto.getUsername();
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("ERROR : This ID already exists");
        }
        String nickname = requestDto.getNickname();
        Optional<Users> foundNickname = userRepository.findByUsername(nickname);
        if (foundNickname.isPresent()) {
            throw new IllegalArgumentException("ERROR : Nickname already EXISTS");
        }

        // 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());
        String passwordCheck = passwordEncoder.encode(requestDto.getPassword());

        Users user = new Users(username, password, passwordCheck, nickname);
        userRepository.save(user);
    }
}
