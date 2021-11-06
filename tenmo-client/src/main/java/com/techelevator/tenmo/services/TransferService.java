package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.view.ConsoleService;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    private String token = null;
    public void setToken(String token) {
        this.token = token;
    }

    public TransferService() {
    }

    public Boolean addTransfer(Transfer transfer, int accountFromId, int accountToId) throws RestClientResponseException, ResourceAccessException {
        Boolean transferResponse = false;
        try {
            transferResponse = restTemplate.exchange(API_BASE_URL + "transfers/" +
                            accountFromId + "/" + accountToId,
                    HttpMethod.POST,
                    makeTransferEntity(transfer),
                    Boolean.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transferResponse;
    }

    public Transfer[] listTransfers(int accountId) throws RestClientResponseException, ResourceAccessException {
        Transfer[] transfers = null;
        try {

            transfers = restTemplate.exchange(API_BASE_URL + "/transfers/history/" + accountId, HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Didn't work, but that's okay. Keep going. You're doing great!");
        }
        return transfers;
    }

    public Transfer transferDetails(int transferId) throws RestClientResponseException, ResourceAccessException {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "/transfers/" + transferId, HttpMethod.GET,
                    makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Didn't work, but that's okay. Keep going. You're doing great!");
        }
        return transfer;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
