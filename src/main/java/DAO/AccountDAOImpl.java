package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AccountDAOImpl implements AccountDAO 
{
    /// table account || values int acount_id (auto_incrememented), varchar username, varchar password

    
    @Override
    public Account insertAccount(Account newAccount) 
    {
        // get connection
        Connection conn = ConnectionUtil.getConnection();

        // try to send account info to database
        try
        {
            // set up sql statement
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, newAccount.getUsername());
            ps.setString(2, newAccount.getPassword());

            ps.executeUpdate();

            // get new instantiated account id
            ResultSet gKeyResultSet = ps.getGeneratedKeys();

            // if id exists
            if (gKeyResultSet.next())
            {
                // get account id from generated results and cast to int
                int accountId = (int) gKeyResultSet.getLong(1);
                Account account = new Account(accountId, newAccount.getUsername(), newAccount.getPassword());
                // return account
                return account;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // return null after catch runs
        return null;
    }

    @Override
    public List<Account> getAllAccounts()
    {
        List<Account> accounts = new ArrayList<>();

        // get connection
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            // setup prepared statement
            String sql = "SELECT * FROM account";
            PreparedStatement ps = conn.prepareStatement(sql);
            // get result set
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return accounts;
    }

    @Override
    public Account getAccountByUsername(String username) 
    {
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            String sql = "SELECT * FROM account WHERE username = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updatePassword(Account updatedAccount) 
    {
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            String sql = "UPDATE account SET password = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, updatedAccount.getPassword());
            ps.setString(2, updatedAccount.getUsername());

            int changeCount = ps.executeUpdate();

            if (changeCount == 0)
            {
                return false;
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return true;
    }

    /**
     * @param account Account to be deleted
     * @return true if successfully deleted/ false if no changes made to database
     */
    @Override
    public boolean deleteAccount(Account account) 
    {
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            String sql = "DELETE FROM account WHERE username = ?  AND password = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            int changeCount = ps.executeUpdate();
            if (changeCount == 0)
            {
                return false;
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

}