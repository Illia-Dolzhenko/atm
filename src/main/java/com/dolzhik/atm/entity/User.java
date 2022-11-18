package com.dolzhik.atm.entity;

public record User(
        String account,
        String password,
        Long balance,
        Long overdraft) {

    public long getBalanceWithOverdraft() {
        return balance + overdraft;
    }
}
