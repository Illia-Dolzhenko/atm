package com.dolzhik.atm.service;

import java.util.Optional;

import com.dolzhik.atm.entity.Balance;
import com.dolzhik.atm.entity.User;
import com.dolzhik.atm.entity.Withdrawal;
import com.dolzhik.atm.entity.exception.WithdrawalExeption;

public interface ATMService {
    public Balance getBalance(User user);

    public Withdrawal withdraw(User user, long amount) throws WithdrawalExeption;

    public Optional<User> authenticateAccount(String account, String password);
}
