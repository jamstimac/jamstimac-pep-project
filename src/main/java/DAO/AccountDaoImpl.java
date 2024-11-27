package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;

public class AccountDaoImpl implements AccountDao {

    @Override
    public Account createAccount(Account newAccount) {
        // get connection
        Connection conn = ConnectionUtil.getConnection();

        // try to send account info to database
        

        // return instantiated account or null
        return null;
    }

    @Override
    public Account getAccountByLogin(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountByLogin'");
    }

    @Override
    public boolean updateAccount(Account updatedAccount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccount'");
    }

    @Override
    public boolean deleteAccountByLogin(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccountByLogin'");
    }

}
