package com.dolzhik.atm.entity;

public record Balance (
    String account,
    Long balance,
    Long overdraft,
    Long maximumWithdrawalAmount
) {
    
}
