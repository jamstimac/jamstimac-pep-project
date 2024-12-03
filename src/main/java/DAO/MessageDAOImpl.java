package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO 
{
    /// table message || message_id int (auto-incremeent), posted_by int (foreign_key to account_id), 
    ///                  message_text varchar, time_posted_epoch long

    @Override
    public Message insertMessage(Message newMessage) 
    {
        // Get connection to database
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            // setup prepared statment
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,newMessage.getPosted_by());
            ps.setString(2, newMessage.getMessage_text());
            ps.setLong(3, newMessage.getTime_posted_epoch());

            // execute update
            ps.executeUpdate();

            // get generated message_id
            ResultSet gKeyResultSet = ps.getGeneratedKeys();
            if(gKeyResultSet.next())
            {
                // cast returned long as an int
                int message_id = (int) gKeyResultSet.getLong(1);

                // instantiate persisted message and return it
                Message msg = new Message(message_id, newMessage.getPosted_by(), newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
                return msg;
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
    public List<Message> getMessages() 
    {
        // Get connection to database
        Connection conn = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {   // Instantiate new Message with results
                Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                
                // add new messages to messages array
                messages.add(msg);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }

    @Override
    public List<Message> getMessages(int posted_by) 
    {
        // Get connection to database
        Connection conn = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try
        {
            // setup prepared statement
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, posted_by);

            // get results set
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                // Instantiate new Message with results
                Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                
                // add new messages to messages array
                messages.add(msg);
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return messages;

    }

    @Override
    public Message getMessage(int message_id) {
        // Get connection to database
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            // setup prepared statement
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, message_id);

            // get result set
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                // instantiate new Message with this info and return it
                Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                
                return msg;
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
    public boolean updateMessage(Message updatedMessage) 
    {
        /// Get connection to database
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            // setup prepared statement
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, updatedMessage.getMessage_text());
            ps.setInt(2, updatedMessage.getMessage_id());

            int changeCount = ps.executeUpdate();

            if (changeCount > 0)
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteMessage(int message_id) 
    {
        // Get connection to database
        Connection conn = ConnectionUtil.getConnection();

        try
        {
            // setup prepared statement
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, message_id);

            int changeCount = ps.executeUpdate();

            if (changeCount > 0)
            {
                return true;
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

}
