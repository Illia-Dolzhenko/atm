package com.dolzhik.atm.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dolzhik.atm.entity.Balance;
import com.dolzhik.atm.entity.User;
import com.dolzhik.atm.entity.Withdrawal;
import com.dolzhik.atm.entity.exception.NotEnoughBanknotesException;
import com.dolzhik.atm.entity.exception.NotEnoughMoneyException;
import com.dolzhik.atm.entity.exception.WithdrawalExeption;
import com.dolzhik.atm.service.ATMService;
import com.dolzhik.atm.service.CashVault;
import com.dolzhik.atm.service.UserBase;

@Service
public class ATM implements ATMService {

    @Autowired
    private CashVault vault;
    @Autowired
    private UserBase base;

    @Override
    public Balance getBalance(User user) {
        return new Balance(user.account(), user.balance(), user.overdraft(), vault.getMaxWithdrawAmount(user));
    }

    @Override
    public Withdrawal withdraw(User user, long amount) throws WithdrawalExeption {

        if (user.getBalanceWithOverdraft() < amount) {
            throw new NotEnoughMoneyException();
        }

        var result = vault.withdrawAmount(amount);

        if (result.isEmpty()) {
            throw new NotEnoughBanknotesException();
        }

        long total = result.get().stream().mapToLong(l -> l).sum();
        long newBalance = user.balance() - total;
        long newOverdraft = user.overdraft();

        if (total > user.balance()) {
            newBalance = 0;
            newOverdraft = user.overdraft() - (total - user.balance());
        }

        base.updateUser(user.account(), new User(user.account(), user.password(), newBalance, newOverdraft));

        return new Withdrawal(newBalance, result.get(), total);
    }

    @Override
    public Optional<User> authenticateAccount(String account, String password) {
        var user = base.getUser(account);
        if (user.isEmpty()) {
            return user;
        }

        if (user.get().password().equals(password)) {
            return user;
        }

        return Optional.empty();
    }
}
