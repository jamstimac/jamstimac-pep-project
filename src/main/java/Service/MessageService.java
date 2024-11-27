package Service;

import DAO.MessageDAOImpl;
import Model.Message;
import java.util.List;

public class MessageService 
{
    // Message DAO
    MessageDAOImpl mDAO;

    /**
     * public general constructor.
     * Initializes MessageDAOImpl
     */
    public MessageService()
    {
        mDAO = new MessageDAOImpl();
    }

    /**
     * Constructor when the DAO layer is provided
     * @param mDAO
     */
    public MessageService(MessageDAOImpl mDAO)
    {
        this.mDAO = mDAO;
    }

    public Message insertMessage(Message newMessage)
    {
        Message msg = mDAO.insertMessage(newMessage);

        return msg;
    }

    public List<Message> getMessages()
    {
        List<Message> messages = mDAO.getMessages();

        return messages;
    }

    public List<Message> getMessages(int posted_by)
    {
        List<Message> messages = mDAO.getMessages(posted_by);

        return messages;
    }

    public Message getMessage(int posted_by, long time_posted_epoch)
    {
        Message msg = mDAO.getMessage(posted_by, time_posted_epoch);

        return msg;
    }

    public boolean updateMessage(Message updatedMessage)
    {
        boolean didUpdate = mDAO.updateMessage(updatedMessage);

        return didUpdate;
    }

    public boolean deleteMessage(int message_id)
    {
        boolean didDelete = mDAO.deleteMessage(message_id);

        return didDelete;
    }
}
