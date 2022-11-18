package com.dolzhik.atm.service;

import java.util.Optional;

import com.dolzhik.atm.entity.User;

public interface UserBase {
    public Optional<User> getUser(String account);

    public void updateUser(String account, User user);
}
