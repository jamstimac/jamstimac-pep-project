package DAO;

import Model.Message;
import java.util.List;

public interface MessageDao {

    /// Create new messages
    public Message createMessage (Message newMessage);

    /// Get messages
    public List<Message> getAllMessages();
    public Message getMessageByUser(int postedBy);

    /// Update messages
    public boolean updateMessage(Message updatedMessage);

    /// Delete messages
    public boolean deleteAccountByLogin(int messageId);

}
