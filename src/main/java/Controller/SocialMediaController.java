package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;

import Model.*;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import java.util.stream.Collectors;

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
        
        app.post("/register", SocialMediaController::postNewUser);
        app.post("/login", SocialMediaController::verifyUserLogin);

        app.post("/messages", SocialMediaController::postNewMessage);

        app.get("/messages", SocialMediaController::getMessages);
        app.get("/messages/{message_id}", SocialMediaController::getMessageByID);
        app.get("/accounts/{account_id}/messages", SocialMediaController::getMessagesByUser);

        app.delete("/messages/{message_id}", SocialMediaController::deleteMessageByID);

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
            context.status(401);
            return;
        }
    }

    /// endpoint POST 8080/messages post new message
    /**
     * 
     * @param context the Javalin Context obj manages information about request and response.
     */
    public static void postNewMessage(Context context){
        try {
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
            context.status(400);
            return;
        }

    } 

    /// endpoint GET 8080/messages get all messages
    /**
     * Get all messages from the database even if no messages exist
     * @param context
     */
    public static void getMessages(Context context){
        List<Message> messages = mService.getMessages();

        context.json(messages);
    }


    /// endpoint GET 8080/messages/{message_id} get message by message_id
    /**
     * 
     * @param context
     */
    public static void getMessageByID(Context context){
        // get all messages
        List<Message> messages = mService.getMessages();

        // get message_id from context
        int msg_id = Integer.parseInt(context.pathParam("message_id"));

        // use a stream to return a new message array containing our singular message
        Message[] userMessage = messages.stream()
        .filter(msg -> msg.getMessage_id() == msg_id)
        .toArray(Message[]::new);

        // if message found
        if (userMessage.length > 0)
        {
            // return that message
            context.json(userMessage[0]);
        }
        else 
        {
            // else return empty string
            context.result("");
        }
    }

    /// endpoint 8080/accounts/{account_id}/messages get message by account_id
    /**
     * 
     * @param context
     */
    public static void getMessagesByUser(Context context){
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        List<Message> msgsByUser = mService.getMessages(account_id);

        context.json(msgsByUser);
        
    }

    /// endpoint DELETE 8080/messages/{message_id} delete message by message_id
    /**
     * 
     * @param context
     */
    public static void deleteMessageByID(Context context){}

    /// endpoint PATCH 8080/messages/{message_id} update message by message_id
    /**
     * 
     * @param context
     */
    public static void updateMessage(Context context){}


    /// FUNCTIONAL METHODS ///
    /**
     * Check if a username is within the database.
     * @param accToCheck
     * @return true if username passed is within database, false otherwise
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
     * @return true if stream is empty or password matching account is correct
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