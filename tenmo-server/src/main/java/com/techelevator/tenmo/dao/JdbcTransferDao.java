package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean completeTransfer(Transfer transfer, int accountFromId, int accountToId) {
        String sql = "INSERT INTO transfers " +
                "(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?);" +
                "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?; " +
                "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";

        return jdbcTemplate.update(sql,
                transfer.getTransferTypeId(),
                transfer.getTransferStatusId(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount(),
                transfer.getAmount(),
                accountFromId,
                transfer.getAmount(),
                accountToId) == 3;
    }

    @Override
    public Transfer transferDetails(int transferId) {
        Transfer details = null;
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            details = mapRowToTransfer(results);
        }
        return details;
    }

    @Override
    public List<Transfer> transferHistory(Long account_id) {
        List<Transfer> previousTransfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id, account_id);
        while (results.next()) {
            previousTransfers.add(mapRowToTransfer(results));
        }
        return previousTransfers;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM users;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
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

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }

}
