package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};


    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private Account account;
    private TransferService transferService;

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(), new TransferService());
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService, TransferService transferService) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
        this.transferService = transferService;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() {

        BigDecimal balance = accountService.getBalance(currentUser.getUser().getId());
        if (balance != null) {
            System.out.println("Your current balance is: " + balance);
        }

    }

    private void viewTransferHistory() {
        String username = null;
        String toUser = null;
        User[] users = transferService.listUsers();
        Transfer[] transfers = transferService.listTransfers(accountService.getAccountId(currentUser.getUser().getId()));
        if (transfers != null) {
            System.out.println("--------------------------\n" +
                    "Transfer\n" +
                    "ID               From/To           Amount\n" +
                    "---------------------------");
            for (Transfer transfer : transfers) {
                if (users != null) {
                    for (User user : users) {
                        if (accountService.getAccountId(user.getId()).equals(transfer.getAccountTo()) && !currentUser.getUser().getId().equals(user.getId())) {
                            username = user.getUsername();
                        }
                        if (accountService.getAccountId(user.getId()).equals(transfer.getAccountFrom()) && !currentUser.getUser().getId().equals(user.getId())) {
                            username = user.getUsername();
                        }
                    }
                    System.out.print(transfer.getTransferID() + "       ");
                    if (accountService.getAccountId(currentUser.getUser().getId()).equals(transfer.getAccountFrom())) {
                        System.out.print("To:   " + username + "         \\$ " + transfer.getAmount() + "\n");
                    } else {
                        System.out.print("From: " + username + "         \\$ " + transfer.getAmount() + "\n");
                    }
                }
            }
            System.out.println("----------------------");
            Integer transferChoice = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
            for (Transfer transfer : transfers) {
                if (transferChoice.equals(transfer.getTransferID())) {
                    Transfer chosenTransfer = transferService.transferDetails(transferChoice);

                    System.out.println("----------------------------");
                    System.out.println("Transfer Details            ");
                    System.out.println("----------------------------");
                    System.out.println("Id: " + chosenTransfer.getTransferID());
                    if (users != null) {
                        for (User user : users) {
                            if (accountService.getAccountId(user.getId()).equals(transfer.getAccountFrom())) {
                                username = user.getUsername();
                                System.out.println("From: " + username);
                            }
                            if (accountService.getAccountId(user.getId()).equals(transfer.getAccountTo())) {
                                toUser = user.getUsername();
                                System.out.println("To: " + toUser);
                            }
                        }
                    }
                    System.out.println("Type: Send");
                    System.out.println("Status: Approved");
                    System.out.println("Amount: \\$" + transfer.getAmount());

                }
            }

        }

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        Transfer transfer = new Transfer();
        User[] users = transferService.listUsers();
        if (users != null) {
            System.out.println("-----------------------\n" +
                    "Users\n" +
                    "ID            Name\n" +
                    "-----------------------");
            for (User user : users) {
                System.out.println(user.toString());
            }
            System.out.println("--------");
        }

        Integer userToId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
        while (userToId.equals(currentUser.getUser().getId())) {
            userToId = console.getUserInputInteger("Cannot send money to yourself. Enter ID of user you are sending to (0 to cancel)");
        }


        BigDecimal amount = console.getUserInputBigDecimal("Enter amount");

        while (accountService.getBalance(currentUser.getUser().getId()).compareTo(amount) < 0) {
            amount = console.getUserInputBigDecimal("Not enough money, please enter amount");
        }

        Integer accountFromId = accountService.getAccountId(currentUser.getUser().getId());
        Integer accountToId = accountService.getAccountId(userToId);

        transfer.setTransferTypeId(2);
        transfer.setTransferStatusId(2);
        transfer.setAccountFrom(accountFromId);
        transfer.setAccountTo(accountToId);
        transfer.setAmount(amount);

        transferService.addTransfer(transfer, accountFromId, accountToId);
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
                String token = currentUser.getToken();
                if (token != null) {
                    accountService.setAuthToken(token);
                    transferService.setAuthToken(token);
                } else {
                    System.out.println("WRONG no token");
                }
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }
}
