package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean completeTransfer(Transfer transfer, int accountFromId, int accountToId){ // boolean to assure it worked? we could also do transfer and have it return that
        String sql = "INSERT INTO transfers " +  // first, creates a transfer in the database
                "(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?);" + //+
                "UPDATE accounts " +                 // updates the balance in fromAccount
                "SET balance = balance - ? " +
                "WHERE account_id = ?; " +
                "UPDATE accounts " +                 // updates the balance in toAccount
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";

//        Long newId = jdbcTemplate.queryForObject(sql,
//                Long.class,
//                transfer.getTransferTypeId(),
//                transfer.getTransferStatusId(),
//                transfer.getAccountFrom(),
//                transfer.getAccountTo(),
//                transfer.getAmount(),
//                transfer.getAmount(),
//                userFromId);
//
//        transfer.setTransferID(newId);
//        return transfer;

        return jdbcTemplate.update(sql,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount(),
                transfer.getAmount(),
                accountFromId, // we should already have this or easily be able to get it
                transfer.getAmount(),
                accountToId) == 3; // because three things will have been updated (???), and we should already have accountToId (or be able to get it)
    }

    @Override
    public Transfer transferDetails(int transferId){
        Transfer details = null;
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()){
            details = mapRowToTransfer(results);
        }
        return details;
    }

    @Override
    public List<Transfer> transferHistory(Long account_id){
        List<Transfer> previousTransfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id, account_id);
        while(results.next()){
            previousTransfers.add(mapRowToTransfer(results));
        }
        return previousTransfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferID(rs.getLong("transfer_id"));
        transfer.setTransferTypeId(rs.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rs.getLong("transfer_status_id"));
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

}
