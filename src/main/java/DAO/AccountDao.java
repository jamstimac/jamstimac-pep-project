package DAO;

import Model.Account;
// import java.util.List;

public interface AccountDao {

    /// Create new accounts
    public Account createAccount (Account newAccount);

    /// Get accounts
    // public List<Account> getAllAccounts();
    public Account getAccountByLogin(String username, String password);

    /// Update Accounts
    public boolean updateAccount(Account updatedAccount);

    /// Delete Accounts
    public boolean deleteAccountByLogin(String username, String password);
}
