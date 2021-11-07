package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface TransferDao {

    boolean completeTransfer(Transfer transfer, int accountFromId, int accountToId);

    Transfer transferDetails(int transferId);

    List<Transfer> transferHistory(Long user_id);

    List<User> findAll();

    int findIdByUsername(String username);

}
