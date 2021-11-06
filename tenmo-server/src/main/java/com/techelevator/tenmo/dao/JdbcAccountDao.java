package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(int id) {
        Account account = new Account();
        String sql = "SELECT * " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapRowToAccount(rowSet);
        }
        return account;
    }


    @Override
    public Long getAccountIdSQL(int id) {
        Account account = new Account();
        String sql = "SELECT * " +
                "FROM accounts " +
                "WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            Account result = mapRowToAccount(rowSet);
            System.out.println(result);
            return result.getAccountId();

        }
        return 0L;
    }

    @Override
    public BigDecimal getBalance(int id) {
        String sql = "SELECT balance " +
                "FROM accounts " +
                "WHERE user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
        return balance;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setUserId(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }


}
