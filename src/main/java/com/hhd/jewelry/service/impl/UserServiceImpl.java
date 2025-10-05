package com.hhd.jewelry.service.impl;

import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.repository.UserRepository;
import com.hhd.jewelry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
