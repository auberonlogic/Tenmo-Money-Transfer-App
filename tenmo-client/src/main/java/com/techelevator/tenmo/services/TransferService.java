package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    private String authToken;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

    public User[] listUsers() throws RestClientResponseException, ResourceAccessException {
        User[] users = null;
        try {

            users = restTemplate.exchange(API_BASE_URL + "/user", HttpMethod.GET,
                    makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Didn't work, but that's okay. Keep going. You're doing great!");
        }
        return users;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
