package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        

        return app;
    }

    /// endpoint POST 8080/register create new account
    /**
     * Creates a new user within register. Usernames must be
     * 4 characters long and an account with this username 
     * should not already exist. Should respond with 200 OK
     * status (will respond with 400 status if unsuccessful)
     * @param context the Javalin Context obj manages information about request and response.
     */
    public void postNewUser(Context context){}
    
    /// endpoint POST 8080/login verify login
    /**
     * Verifies login based on password and username passed into the request body.
     * Responds with 200 OK status (will respoond with 401 status if unsuccessful).
     * @param context the Javalin Context obj manages information about request and response.
     */
    public void verifyUserLogin(Context context){}

    /// endpoint POST 8080/messages post new message
    /**
     * 
     * @param context the Javalin Context obj manages information about request and response.
     */
    public void postNewMessage(Context context){} 

    /// endpoint GET 8080/messages get all messages
    /**
     * 
     * @param context
     */
    public void getMessages(Context context){}


    /// endpoint GET 8080/messages/{message_id} get message by message_id
    /**
     * 
     * @param context
     */
    public void getMessageByID(Context context){}

    /// endpoint 8080/accounts/{account_id}/messages get message by account_id
    /**
     * 
     * @param context
     */
    public void getMessagesByUser(Context context){}

    /// endpoint DELETE 8080/messages/{message_id} delete message by message_id
    /**
     * 
     * @param context
     */
    public void deleteMessageByID(Context context){}

    /// endpoint PATCH 8080/messages/{message_id} update message by message_id
    /**
     * 
     * @param context
     */
    public void updateMessage(Context context){}


}