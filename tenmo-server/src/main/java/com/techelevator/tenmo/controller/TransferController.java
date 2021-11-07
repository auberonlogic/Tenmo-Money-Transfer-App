package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private JdbcTransferDao dao;

    public TransferController(JdbcTransferDao dao, JdbcUserDao userDao) {
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

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> list() {
        return dao.findAll();
    }

    @RequestMapping(value = "/user/names/{username}", method = RequestMethod.GET)
    public int findIdByUsername(User user, @PathVariable String username) {
        return dao.findIdByUsername(user.getUsername());
    }

}
