package Service;

import Model.Account;

public interface AccountService {
    

    Account createNewAccount(Account account);

    Account login(Account account);

}


