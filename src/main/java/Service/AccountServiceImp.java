package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountServiceImp implements AccountService{


    /**
      - The registration will be successful if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
      - If the registration is not successful, the response status should be 400. (Client error) 
     */

     AccountDAO accountDAO = new AccountDAO();

    @Override
    public Account createNewAccount(Account account) {
        if(account.getUsername().isBlank())return null;
        if(account.getPassword().length() < 4)return null;

        return accountDAO.createNewAccount(account.getUsername(), account.getPassword());
    }

    @Override
    public Account login(Account account) {
        if(account.getUsername().isBlank() || account.getPassword().isBlank())return null;

        return accountDAO.login(account.getUsername(), account.getPassword());
    }
    
}
