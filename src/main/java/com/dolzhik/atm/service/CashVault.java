package com.dolzhik.atm.service;

import java.util.List;
import java.util.Optional;

import com.dolzhik.atm.entity.User;

public interface CashVault {
    public long getMinBanknote();

    public Optional<List<Long>> withdrawAmount(long amount);

    public long getTotalBalance();

    public long getMaxWithdrawAmount(User user);
}
