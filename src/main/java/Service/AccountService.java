package Service;

import DAO.AccountDAOImpl;
import Model.Account;
import java.util.List;


public class AccountService 
{
    AccountDAOImpl aDAO;

    /**
     * public general constructor.
     * Initializes AccountDAOImpl
     */
    public AccountService()
    {
        aDAO = new AccountDAOImpl();
    }

    /**
     * Constructor when the DAO layer is provided
     * @param aDAO
     */
    public AccountService(AccountDAOImpl aDAO)
    {
        this.aDAO = aDAO;
    }

    /**
     * Return a persisted account after adding it to the database
     * @param newAccount Account object
     * @return new Account object with account_id
     */
    public Account insertAccount(Account newAccount)
    {
        Account retAccount = aDAO.insertAccount(newAccount);

        return retAccount;
    }

    /**
     * Returns a list of all accounts or an emty arrayList.
     * @return arrayList<account>
     */
    public List<Account> getAccounts()
    {
        List<Account> accounts = aDAO.getAccounts();

        return accounts;
    }

    /**
     * Takes a username from input and returns the appropriate account
     * @param username
     * @return Account obj
     */
    public Account getAccountByUsername(String username)
    {
        Account retAccount = aDAO.getAccountByUsername(username);

        return retAccount;
    }

    /**
     * Updates password based on userAccount credentials
     * @param updatedAccount Account obj
     * @param newPassword string
     * @return didUpdate boolean 
     */
    public boolean updatePassword(Account updatedAccount, String newPassword)
    {
        boolean didUpdate = aDAO.updatePassword(updatedAccount, newPassword);

        return didUpdate;
    }

    /**
     * Delete account from table in database.
     * @param account Account obj
     * @return boolean didDelete
     */
    public boolean deleteAccount(Account account)
    {
        boolean didDelete = aDAO.deleteAccount(account);

        return didDelete;
    }
}
