package com.dolzhik.atm.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dolzhik.atm.entity.User;
import com.dolzhik.atm.service.UserBase;

@Service
public class InMemoryUserBase implements UserBase {

    private Environment environment;
    private Map<String, User> users;

    public InMemoryUserBase(@Autowired Environment environment) {
        this.environment = environment;
        this.users = new HashMap<>();
        init();
    }

    @Override
    public Optional<User> getUser(String account) {
        return Optional.ofNullable(users.get(account));
    }

    @Override
    public void updateUser(String account, User user) {
        users.put(account, user);
    }

    private void init() {
        var property = Optional.ofNullable(environment.getProperty("user_accounts"));
        if (property.isEmpty()) {
            return;
        }

        for (String section : property.get().split(";")) {
            String[] splitSection = section.split(",");
            if (splitSection.length < 4) {
                return;
            }

            long balance = Optional.ofNullable(splitSection[2]).map(Long::parseLong).orElse(0L);
            long overdraft = Optional.ofNullable(splitSection[3]).map(Long::parseLong).orElse(0L);
            User user = new User(splitSection[0], splitSection[1], balance, overdraft);
            users.put(user.account(), user);
        }

    }

}
