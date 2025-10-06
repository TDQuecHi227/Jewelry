package com.hhd.jewelry.service;

import com.hhd.jewelry.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getByEmail(String email);
}
