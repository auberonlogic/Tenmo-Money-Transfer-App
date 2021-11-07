package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccount(int id);

    Long getAccountIdSQL(int id);

    BigDecimal getBalance(int id);


}
