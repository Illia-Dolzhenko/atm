package com.dolzhik.atm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dolzhik.atm.entity.User;
import com.dolzhik.atm.service.CashVault;

@Service
public class InMemoryCashVault implements CashVault {

    private Environment environment;
    private Map<Long, Long> banknotes;
    private Logger logger = LoggerFactory.getLogger(InMemoryCashVault.class);

    public InMemoryCashVault(@Autowired Environment environment) {
        this.environment = environment;
        this.banknotes = new HashMap<>();
        init();
    }

    @Override
    public long getMinBanknote() {
        return banknotes.keySet().stream().mapToLong(l -> l).min().orElse(0L);
    }

    @Override
    public Optional<List<Long>> withdrawAmount(long amount) {
        logger.info("Trying to withdraw: " + amount);
        if (amount > getTotalBalance() || amount % getMinBanknote() != 0) {
            logger.error("Not enough cash in the vault.");
            return Optional.empty();
        }
        List<Long> banknoteList = banknotes.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e1.getKey(), e2.getKey())).map(Entry::getKey).toList();

        int biggestBanknoteIndex = banknoteList.size() - 1;
        long currentBanknote = banknoteList.get(biggestBanknoteIndex);
        List<Long> result = new ArrayList<>();

        while (amount > 0) {
            if (banknotes.get(currentBanknote) > 0 && amount >= currentBanknote) {
                amount = Long.sum(amount, -currentBanknote);
                result.add(currentBanknote);
                banknotes.put(currentBanknote, banknotes.get(currentBanknote) - 1);
            } else {
                biggestBanknoteIndex--;
                if (biggestBanknoteIndex < 0) {
                    break;
                }
            }
            currentBanknote = banknoteList.get(biggestBanknoteIndex);
        }

        return Optional.of(result);
    }

    @Override
    public long getTotalBalance() {
        return banknotes.entrySet().stream().mapToLong(entry -> entry.getKey() * entry.getValue()).sum();
    }

    @Override
    public long getMaxWithdrawAmount(User user) {
        long userBalance = user.getBalanceWithOverdraft();

        if (userBalance > getTotalBalance()) {
            return getTotalBalance();
        }
        userBalance = (long) (userBalance / getMinBanknote()) * getMinBanknote();

        return userBalance;
    }

    private void init() {
        var banknotesProperty = Optional.ofNullable(environment.getProperty("banknotes"));

        if (banknotesProperty.isEmpty()) {
            return;
        }

        for (String section : banknotesProperty.get().split(",")) {
            String[] splitSection = section.split("x");
            long amount = Optional.ofNullable(splitSection[0]).map(Long::parseLong).orElse(0L);
            long banknote = Optional.ofNullable(splitSection[1]).map(Long::parseLong).orElse(0L);

            if (banknote != 0 && amount != 0) {
                banknotes.put(banknote, amount);
            }

        }
    }
}
