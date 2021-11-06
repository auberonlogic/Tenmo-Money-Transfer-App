package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public AccountService() { }

    public Account getAccount(int accountId) {
        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "account/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error");
        }
        return account;
    }

    public Integer getAccountId(int userId) {
        Integer accountId = null;
        try {
            accountId = restTemplate.exchange(API_BASE_URL + "accountId/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(), Integer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error");
        }
        return accountId;
    }

    public BigDecimal getBalance(Integer userId) throws RestClientResponseException, ResourceAccessException {
        BigDecimal balance = null;
        try {
            balance = restTemplate.exchange(API_BASE_URL + "account/balance/" + userId,
                            HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error");
        }
        return balance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }




}
