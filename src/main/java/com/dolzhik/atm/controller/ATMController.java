package com.dolzhik.atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dolzhik.atm.entity.Response;
import com.dolzhik.atm.entity.exception.NotEnoughBanknotesException;
import com.dolzhik.atm.entity.exception.NotEnoughMoneyException;
import com.dolzhik.atm.entity.exception.WithdrawalExeption;
import com.dolzhik.atm.service.ATMService;

@RestController
public class ATMController {

    @Autowired
    private ATMService atmService;

    @GetMapping("/balance")
    public ResponseEntity<Response> getBalance(@RequestParam String account, @RequestParam String password) {
        var user = atmService.authenticateAccount(account, password);
        if (user.isEmpty()) {
            return ResponseEntity.ok(new Response("Invalid account or password.", null));
        }

        return ResponseEntity.ok(new Response("Balance request is successful.", atmService.getBalance(user.get())));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response> withdraw(@RequestParam String account, @RequestParam String password,
            @RequestParam Long amount) {
        var user = atmService.authenticateAccount(account, password);
        if (user.isEmpty()) {
            return ResponseEntity.ok(new Response("Invalid account or password.", null));
        }

        try {
            var withdrawal = atmService.withdraw(user.get(), amount);
            return ResponseEntity.ok(new Response("Withdrawal request is successful.", withdrawal));
        } catch (NotEnoughMoneyException e) {
            return ResponseEntity.ok(new Response("You don't have enough money on you balance.", null));
        } catch (NotEnoughBanknotesException e) {
            return ResponseEntity.ok(new Response("Not enough cash in the ATM.", null));
        } catch (WithdrawalExeption e) {
            return ResponseEntity.ok(new Response("Withdrawal request is failed.", null));
        }
    }
}
