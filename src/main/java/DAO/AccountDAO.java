package DAO;

import java.util.List;
import Model.Account;
// import java.util.List;

public interface AccountDAO 
{

    /// Create new accounts
    /**
     * Takes new account supplied as paramater and tries to add to database 
     * @param newAccount Account obj
     * @return Acount obj with generated account_id, returns null if the account wasn't substatiated
     */
    public Account insertAccount (Account newAccount);

    /// Get accounts
    /**
     * @return List of all accounts in database
     */
    public List<Account> getAccounts();

    /**
     * Takes username to search database for account
     * @param username string given by user
     * @return Account with associated username
     */
    public Account getAccountByUsername(String username);

    /// Update Accounts
    /**
     * Updates password through passed Account object.
     * @param updatedAccount Account obj with updated password
     * @return true if successful/false if no changes were made to database
     */
    public boolean updatePassword(Account updatedAccount);

    /// Delete Accounts
    /**
     * Deletes account from database.
     * @param account Account to be deleted
     * @return true if successfully deleted/ false if no changes made to database
     */
    public boolean deleteAccount(Account account);
}
