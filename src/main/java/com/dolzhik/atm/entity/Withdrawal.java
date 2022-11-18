package com.dolzhik.atm.entity;

import java.util.List;

public record Withdrawal(
        Long remainingBalance,
        List<Long> banknotes,
        Long totalAmount) {

}
