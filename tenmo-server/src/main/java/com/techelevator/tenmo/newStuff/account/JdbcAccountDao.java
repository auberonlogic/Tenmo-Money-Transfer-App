//package com.techelevator.tenmo.newStuff.account;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.rowset.SqlRowSet;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//
//@Component
//public class JdbcAccountDao implements AccountDao{
//
//    private JdbcTemplate jdbcTemplate;
//
//    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public Account getAccount(int id) {
//        Account account = new Account();
//        String sql = "SELECT account_id, user_id, balance " +
//                "FROM accounts " +
//                "WHERE user_id = ?;";
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
//        if (rowSet.next()) {
//            Account result = mapRowToAccount(rowSet);
//            System.out.println(result);
//            return result;
//
//        }
//        return account;
//    }
//
//    private Account mapRowToAccount(SqlRowSet rs) {
//        Account account = new Account();
//        account.setAccountId(rs.getLong("account_id"));
//        account.setUserId(rs.getLong("user_id"));
//        account.setBalance(rs.getBigDecimal("balance"));
//        return account;
//    }
//
//
//}
