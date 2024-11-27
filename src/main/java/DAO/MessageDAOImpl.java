package DAO;

import java.util.List;
import Model.Message;

public class MessageDAOImpl implements MessageDAO 
{

    @Override
    public Message insertMessage(Message newMessage) 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createMessage'");
    }

    @Override
    public List<Message> getMessages() 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMessages'");
    }

    @Override
    public List<Message> getMessages(int posted_by) 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessageByUser'");
    }

    @Override
    public Message getMessage(int posted_by, long time_posted_epoch) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessage'");
    }

    @Override
    public boolean updateMessage(Message updatedMessage) 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMessage'");
    }

    @Override
    public boolean deleteAccountByLogin(int message_id) 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccountByLogin'");
    }

}
