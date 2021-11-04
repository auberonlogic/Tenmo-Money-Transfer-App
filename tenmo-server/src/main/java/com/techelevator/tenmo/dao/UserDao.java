package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.newStuff.account.Account;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    Account getAccount(int id);

    BigDecimal getBalance(int id);

    Transfer createTransfer(Transfer transfer);

    List<Transfer> getAllTransfers();

    Transfer getSpecificTransfer(int id);
}
