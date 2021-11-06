package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private JdbcTransferDao dao;

    public TransferController(JdbcTransferDao dao) {
        this.dao = dao;
    }


    @RequestMapping(value = "transfers/{accountFromId}/{accountToId}", method = RequestMethod.POST)
    public boolean completeTransfer(@RequestBody Transfer transfer, @PathVariable int accountFromId, @PathVariable int accountToId){
        return dao.completeTransfer(transfer, accountFromId, accountToId);
    }


    @RequestMapping(value = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer transferDetails(@PathVariable int transferId){
        return dao.transferDetails(transferId);
    }


    @RequestMapping(value = "/transfers/history/{id}", method = RequestMethod.GET)
    public List<Transfer> previousTransfer(@PathVariable Long id) {
        return dao.transferHistory(id);
    }

//


}
