package DAO;

import Model.Message;
import java.util.List;

public interface MessageDAO 
{

    /// Create new messages
    /**
     * Tries to add a message to the database.
     * @param newMessage Message
     * @return new substantiated message (null if unsuccessful)
     */
    public Message insertMessage(Message newMessage);

    /// Get messages
    /**
     * Tries to get all messages in database.
     * @return ArrayList of all messages in database (empty array if not found)
     */
    public List<Message> getMessages();

    /**
     * Tries to get all messages by a specific user.
     * @param posted_by int
     * @return List of Messages within database (empty array if not found)
     */
    public List<Message> getMessages(int posted_by);

    /**
     * Tries to get a singular message posted by a user at a specific time.
     * @param posted_by int
     * @param time_posted_epoch long
     * @return A Message within the database (null if not found)
     */
    public Message getMessage(int posted_by, long time_posted_epoch);

    /// Update messages
    /**
     * Contacts database and updates a corresponding message with new text
     * @param updatedMessage Message
     * @return true if successful/ false if no change was made to database
     */
    public boolean updateMessage(Message updatedMessage);

    /// Delete messages
    /**
     * Removes a message from the database
     * @param message_id int
     * @return true if successful/ false if no change was made to database
     */
    public boolean deleteAccountByLogin(int message_id);

}
