package Controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.AccountServiceImp;
import Service.MessageService;
import Service.MessageServiceImp;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

////////////////////////////Move some of the stuff from controller into SERVICE classes if time
////////////////////////////should be passing the context into the Service methods 
////////////////////////////too late now tho do after

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService = new AccountServiceImp();
    MessageService messageService = new MessageServiceImp();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
  
    //The body will contain a representation of a JSON Account, but will not contain an account_id.
    //this:: passses the Context obj
        app.post("register", this::createNewAccount);
        app.post("login", this::login);

        app.post("messages", this::createNewPost);
        app.get("messages", this::getAllMessages);
        app.get("messages/{message_id}", this::getMessage);
        app.delete("messages/{message_id}", this::deleteMessage);

        return app;
    }

    /**
6: Our API should be able to delete a message identified by a message ID.
As a User, I should be able to submit a DELETE request on the endpoint 
DELETE localhost:8080/messages/{message_id}.

The deletion of an existing message should remove an existing message from the database. 
If the message existed, the response body should contain the now-deleted message. 
The response status should be 200, which is the default.
If the message did not exist, the response status should be 200, but the response 
body should be empty. This is because the DELETE verb is intended to be idempotent, 
ie, multiple calls to the DELETE endpoint should respond with the same type of response.
     */
    private void deleteMessage(Context context) {
        String jsonMessageInfo = context.pathParam("message_id");
        int message_id = Integer.parseInt(jsonMessageInfo);

        try{
            Message message = messageService.deleteMessage(message_id);

            if (message != null) {
                context.status(200);  // OK
                context.json(message);
            } 
            else context.status(200);  // Return 200 but with an empty response
            

        }catch(Exception e){
            System.out.println("Exception in SocailMediaController deleteMessage");
            e.printStackTrace();
            context.status(500);                //Server Error
        }
    }


     private void getMessage(Context context) {
        String jsonMessageInfo = context.pathParam("message_id");
        int message_id = Integer.parseInt(jsonMessageInfo);

        try{
            Message message = messageService.getMessage(message_id);

            if (message != null) {
                context.status(200);  // OK
                context.json(message);
            } 
            else context.status(200);  // Return 200 but with an empty response
            

        }catch(Exception e){
            System.out.println("Exception in SocailMediaController getMessage");
            e.printStackTrace();
            context.status(500);                //Server Error
        }
    }
     

     private void getAllMessages(Context context) {

        try{
            List<Message> allMessages = messageService.getAllMessages();

            context.status(200);     //OK
            context.json(allMessages);

        }catch(Exception e){
            System.out.println("Exception in SocailMediaController getAllMessages");
            e.printStackTrace();
            context.status(500);                //Server Error
        }
    }


    private void createNewPost(Context context) {
        String jsonMessageInfo = context.body();
        ObjectMapper objM = new ObjectMapper();

        try{
            Message message = objM.readValue(jsonMessageInfo, Message.class);
            Message createdMessage = messageService.createNewPost(message);

            if(createdMessage != null){
                context.status(200);     //OK
                context.json(createdMessage);
            }
            else context.status(400); 
        }catch(Exception e){
            System.out.println("Exception in SocailMediaController createNewPost");
            e.printStackTrace();
            context.status(500);                //Server Error
        }
    }


    private void login(Context context){
        String jsonAccountInfo = context.body();
        ObjectMapper objM = new ObjectMapper();

        try{
            Account account = objM.readValue(jsonAccountInfo, Account.class);
            Account loggedInAccount = accountService.login(account);

            if(loggedInAccount != null){
                context.status(200);     //OK
                context.json(loggedInAccount);
            }
            else context.status(401);           //Client Error Unauthorized

        }catch(Exception e){
            System.out.println("Exception in SocailMediaController Login");
            e.printStackTrace();
            context.status(500);                //Server Error
        }
    }
    

    private void createNewAccount(Context context){
        String jsonAccountInfo = context.body();
        ObjectMapper objM = new ObjectMapper();        // V V V Convert jsonAccountInfo String into Account OBJ
        
        
        try{
            Account account = objM.readValue(jsonAccountInfo, Account.class); 
            Account createdAccount = accountService.createNewAccount(account);             //Call createNewAccount service method
            
            if(createdAccount != null){
                context.status(200);     //OK
                context.json(createdAccount);
            }
            else context.status(400);           //Client Error
        }
        catch(Exception e){
            System.out.println("Exception in SocailMediaController createNewAccount");
            e.printStackTrace();
            context.status(500);                //Server Error
        }
    }






}






//Examples
// app.get("example-endpoint", this::exampleHandler);

//     /**
//      * This is an example handler for an example endpoint.
//      * @param context The Javalin Context object manages information about both the HTTP request and response.
//      */
//     private void exampleHandler(Context context) {
//         context.json("sample text");
//     }