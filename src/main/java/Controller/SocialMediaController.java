package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import Model.*;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private static AccountService aService = new AccountService();
    private static MessageService mService = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        /// post handlers
        // account
        app.post("/register", SocialMediaController::postNewUser);
        app.post("/login", SocialMediaController::verifyUserLogin);
        // message
        app.post("/messages", SocialMediaController::postNewMessage);

        /// get handlers
        // message
        app.get("/messages", SocialMediaController::getMessages);
        app.get("/messages/{message_id}", SocialMediaController::getMessageByID);
        app.get("/accounts/{account_id}/messages", SocialMediaController::getMessagesByUser);

        /// delete handlers
        // message
        app.delete("/messages/{message_id}", SocialMediaController::deleteMessageByID);

        /// patch handlers
        // message
        app.patch("/messages/{message_id}", SocialMediaController::updateMessage);

        return app;
    }

    /// HANDLERS ///
    /// endpoint POST 8080/register create new account
    /**
     * Creates a new user within register. Usernames must be
     * 4 characters long and an account with this username 
     * should not already exist. Should respond with 200 OK
     * status (will respond with 400 status if unsuccessful)
     * @param context the Javalin Context obj manages information about request and response.
     */
    public static void postNewUser(Context context){
        
        try{
            // returns a new account from context if json returns an account.class
            // if username in context is not within database already
            // if password is longer than 4 characters
            // sends a ValidationException if get does not return a validated value
            Account account = context.bodyValidator(Account.class)
            .check(acc -> !(isUsernameWithinDB(acc.getUsername())), "Username already in use")
            .check(acc -> !(acc.getUsername().isBlank()), "Username is blank")
            .check(acc -> acc.getPassword().length() > 4, "Password too short")
            .get();

            // set account in database get new account with primary key
            Account instantiatedAccount = aService.insertAccount(account);

            // post new json object
            context.json(instantiatedAccount);
            

        }
        catch (ValidationException ve)
        {
            // exit method with status code 400
            context.status(400);
            return;
        } 

    }

    /// endpoint POST 8080/login verify login
    /**
     * Verifies login based on password and username passed into the request body.
     * Responds with 200 OK status (will respoond with 401 status if unsuccessful).
     * @param context the Javalin Context obj manages information about request and response.
     */
    public static void verifyUserLogin(Context context){
        try{
            // get entered account from body
            // if username is in database
            // if password matches one in database
            // Validation Exception if input is not validated
            Account accountToBeVerified = context.bodyValidator(Account.class)
            .check(acc -> isUsernameWithinDB(acc.getUsername()), "Username not in database")
            .check(acc -> isPasswordCorrect(acc.getUsername(), acc.getPassword()), "Password is incorrect")
            .get();

            // get full account from database
            Account verifiedAccount = aService.getAccountByUsername(accountToBeVerified.getUsername());
            context.json(verifiedAccount);

        }
        catch (ValidationException ve)
        {
            // exit method with error status 401
            context.status(401);
            return;
        }
    }

    /// endpoint POST 8080/messages post new message
    /**
     * Verifies message text as well as user foreign key to account table. Posts a 
     * message and adds it to the database if the message is within 0 (exclusive) 
     * and 255 (inclusssive). 
     * If either the message text or the foreign key are invalid, a ValidationException
     * is given by the validator body and the context will set a status 400. Otherwise, 
     * a json object will be posted to the path.
     * @param context the Javalin Context obj manages information about request and response.
     */
    public static void postNewMessage(Context context){
        try {
            // validate message from body
            // if message text is not blank
            // if message text is less than or equal to 255 (max length)
            // if account id is found
            // Validation Exception if input is not validated
            Message msg = context.bodyValidator(Message.class)
            .check(mess -> !(mess.getMessage_text().isBlank()), "Message blank")
            .check(mess -> mess.getMessage_text().length() <= 255, "Message too large")
            .check(mess -> checkUserByID(mess.getPosted_by()), "Message poster is not valid")
            .get();

            Message instantiatedMsg = mService.insertMessage(msg);
            context.json(instantiatedMsg);

        }
        catch (ValidationException ve)
        {
            // exits method with status code 400
            context.status(400);
            return;
        }

    } 

    /// endpoint GET 8080/messages get all messages
    /**
     * Get all messages from the database even if no messages exist. 
     * Gets an empty json object if no messages are found. Returns
     * all messages as json object from database if found.
     * @param context
     */
    public static void getMessages(Context context){
        // get all messages
        List<Message> messages = mService.getMessages();
        // set these messages to the json object in result.
        context.json(messages);
    }


    /// endpoint GET 8080/messages/{message_id} get message by message_id
    /**
     * Will always return status 200. Parses message_id from pathParam. 
     * Returns a message found with this id, or, if no message is found,
     * returns an empty string.
     * @param context
     */
    public static void getMessageByID(Context context){
        // get all messages
        // List<Message> messages = mService.getMessages();

        // get message_id from context
        int msg_id = Integer.parseInt(context.pathParam("message_id"));

        // // use a stream to return a new message array containing our singular message
        // Message[] userMessage = messages.stream()
        // .filter(msg -> msg.getMessage_id() == msg_id)
        // .toArray(Message[]::new);

        // get message using service
        Message userMessage = mService.getMessage(msg_id);

        // if message found
        if (userMessage != null)
        {
            // return that message
            context.json(userMessage);
            // exit function
            return;
        }

        // else return empty string
        context.result("");
    }

    /// endpoint 8080/accounts/{account_id}/messages get message by account_id
    /**
     * Gets all messages by a specific user. Does not parse if this used exists.
     * If no messages are found, returns an empty string. Always returns a 200 status.
     * @param context
     */
    public static void getMessagesByUser(Context context){
        // get account id form path 
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        // get messages by user id
        List<Message> msgsByUser = mService.getMessages(account_id);

        // set messages as json object
        context.json(msgsByUser);
        
    }

    /// endpoint DELETE 8080/messages/{message_id} delete message by message_id
    /**
     * Sets context result to json representation of deleted message. If no message
     * was deleted, returns an empty string. Always returns a status of 200.
     * @param context
     */
    public static void deleteMessageByID(Context context){
        // get message id from path
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        // try deleting message
        Message deletedMessage = mService.deleteMessage(message_id);

        // if successful, a message should be returned
        if (deletedMessage != null){
            // set json object in result body. exit function
            context.json(deletedMessage);
            return;
        }
        // else set result to empty string
        context.result("");
    }

    /// endpoint PATCH 8080/messages/{message_id} update message by message_id
    /**
     * Gets message id from pathParam. Maps message_text from body. Verifies these
     * variables, then gets the full message from the database by the message_id,
     * updates the message_text within this message, runs updateMessage on the database,
     * and sets this updated message as the resulting json object. 
     * If message_text length is either 0 or above 255 or the message_id does not return
     * a valid message from the database, the function exits with a status of 400.
     * Otherwise, it returns a status 200 alongside the json represetation of the message.
     * @param context
     */
    public static void updateMessage(Context context){
        // get message id
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        // if message doesn't exist
        if(!(messageExists(message_id)))
        {
            context.status(400).result("");
            return;
        } 

        // setup variables
        String messageTextUpdate = "";
        try {
            // get an object mapper to read the json body
            ObjectMapper om = new ObjectMapper();
            JsonNode jNode = om.readTree(context.body());
            // get message_text from body
            messageTextUpdate = jNode.get("message_text").asText();
        }
        catch(JsonProcessingException e)
        {
            e.printStackTrace();
            // messageTextUpdate stays empty
        }

        // if message text is blank
        if (messageTextUpdate.isBlank())
        {
            // exit function with error code and empty result string
            context.status(400).result("");
            return;
        }
        // if message text is larger than allowed
        if (messageTextUpdate.length() > 255)
        {
            // exit function with error code and empty result string
            context.status(400).result("");
            return;
        }

        // else get original message
        Message msg = mService.getMessage(message_id);
        // set new text
        msg.setMessage_text(messageTextUpdate);
        // update this message
        Message updatedMessage = mService.updateMessage(msg);

        // if this returns an updated message
        if (updatedMessage != null){
            // return it in body
            context.json(updatedMessage);
            return;
        }

        // else error return empty string
        context.status(400).result("");
    }



    /// FUNCTIONAL STATIC METHODS ///
    /// Used in handlers to create more readable code. ///
    /**
     * Returns a boolean if the message exists or not provided a message_id.
     * @param message_id
     * @return true if message is found, false otherwise.
     */
    public static boolean messageExists(int message_id)
    {
        Message msg = mService.getMessage(message_id);

        if (msg != null){
            return true;
        }
        
        return false;
    }

    /**
     * Check if a username is within the database.
     * @param accToCheck
     * @return true if username passed is within database, false otherwise.
     */
    public static boolean isUsernameWithinDB(String newUsername) {
        List<Account> accounts = aService.getAccounts();

        return accounts.stream().anyMatch(acc -> acc.getUsername().equalsIgnoreCase(newUsername));

    }

    /**
     * Filters through all accounts to then check if password is correct.
     * Will return true if stream is empty, so be careful.
     * @param username
     * @param password
     * @return true if stream is empty or password matching account is correct.
     */
    public static boolean isPasswordCorrect(String username, String password)
    {
        // get all accounts
        List<Account> accounts = aService.getAccounts();

        // filter accounts for the current username
        // check if password is correct
        return accounts.stream().filter(acc -> acc.getUsername().equals(username))
        .allMatch(acc -> acc.getPassword().equals(password));
    }

    /**
     * Checks if an account exists. Account id is stored in message as
     * posted by.
     * @param account_id
     * @return true if returned account is not null
     */
    public static boolean checkUserByID(int account_id)
    {
        Account account = aService.getAccount(account_id);

        if (account != null)
        {
            return true;
        }

        return false;
    }
}