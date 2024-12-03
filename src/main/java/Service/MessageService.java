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

    public Message getMessage(int message_id)
    {
        Message msg = mDAO.getMessage(message_id);

        return msg;
    }

    public Message updateMessage(Message updatedMessage)
    {
        boolean didUpdate = mDAO.updateMessage(updatedMessage);

        if(didUpdate)
        {
            return mDAO.getMessage(updatedMessage.getMessage_id());
        }
        else
        {
            return null;
        }
    }

    public Message deleteMessage(int message_id)
    {
        // get message by id
        Message deletedMessage = mDAO.getMessage(message_id);

        // delete message
        boolean didDelete = mDAO.deleteMessage(message_id);

        // if true
        if (didDelete){
            //return deleted message
            return deletedMessage;
        }

        // else return null
        return null;
    }
}
