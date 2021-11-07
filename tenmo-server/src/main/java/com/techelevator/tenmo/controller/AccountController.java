package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao dao;

    public AccountController(AccountDao dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "account/{id}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable int id) {
        return dao.getAccount(id);
    }

    @RequestMapping(value = "/accountId/{id}", method = RequestMethod.GET)
    public Long getAccountId(@PathVariable int id) {
        return dao.getAccountIdSQL(id);
    }

    @RequestMapping(value = "/account/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {
        return dao.getBalance(id);
    }

}
