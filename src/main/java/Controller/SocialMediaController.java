package Controller;

import static org.mockito.ArgumentMatchers.contains;

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

////////////////////////////Clean up further later by
////////////////////////////Passing context into the Service methods and doing data extraction there 
////////////////////////////too late rn do over the weekend

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
        app.patch("messages/{message_id}", this::updateMessage);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByUser);

        return app;
    }


    private void sendResponse(Context context, int status, Object responseBody){
        context.status(status);
        if(responseBody != null) context.json(responseBody);
    }


    private void handleError(Context context, Exception e) {
        e.printStackTrace();
        sendResponse(context, 500, "Internal server error"); // 500 Server Error
    }


    private void getAllMessagesByUser(Context context) {
        String jsonPathInfo = context.pathParam("account_id");
        int account_id = Integer.parseInt(jsonPathInfo);

        try{
            List<Message> allMessagesByUser = messageService.getAllMessagesByUser(account_id);
            sendResponse(context, 200, allMessagesByUser);
        }catch(Exception e){
            handleError(context, e);
        }
    }


     private void updateMessage(Context context) {
        ObjectMapper objM = new ObjectMapper();
        String jsonPathInfo = context.pathParam("message_id");
        int message_id = Integer.parseInt(jsonPathInfo);
        String jsonMessageInfo = context.body();

        try{
            Message message = objM.readValue(jsonMessageInfo, Message.class);
            Message updatedMessage = messageService.updateMessage(message_id, message);

            if (updatedMessage != null) {
                context.status(200);  // OK
                context.json(updatedMessage);
            } 
            else context.status(400);  // Return 400 client error but with an empty response
            
        }catch(Exception e){
            handleError(context, e);
        }
    }


    private void deleteMessage(Context context) {
        String jsonMessageInfo = context.pathParam("message_id");
        int message_id = Integer.parseInt(jsonMessageInfo);

        try{
            Message message = messageService.deleteMessage(message_id);
            sendResponse(context, 200, message);
        }catch(Exception e){
            handleError(context, e);
        }
    }


     private void getMessage(Context context) {
        String jsonPathInfo = context.pathParam("message_id");
        int message_id = Integer.parseInt(jsonPathInfo);

        try{
            Message message = messageService.getMessage(message_id);
            sendResponse(context, 200, message);
        }catch(Exception e){
            handleError(context, e);
        }
    }
     

     private void getAllMessages(Context context) {

        try{
            List<Message> allMessages = messageService.getAllMessages();
            sendResponse(context, 200, allMessages);
        }catch(Exception e){
            handleError(context, e);
        }
    }


    private void createNewPost(Context context) {
        String jsonMessageInfo = context.body();
        ObjectMapper objM = new ObjectMapper();
        int status;

        try{
            Message message = objM.readValue(jsonMessageInfo, Message.class);
            Message createdMessage = messageService.createNewPost(message);

            if(createdMessage != null) status = 200;     //OK 
            else status = 400;  //Client Error Unauthorized

            sendResponse(context, status, createdMessage);
        }catch(Exception e){
            handleError(context, e);
        }
    }


    private void login(Context context){
        String jsonAccountInfo = context.body();
        ObjectMapper objM = new ObjectMapper();
        int status;

        try{
            Account account = objM.readValue(jsonAccountInfo, Account.class);
            Account loggedInAccount = accountService.login(account);

            if(loggedInAccount != null) status = 200;     //OK 
            else status = 401;  //Client Error Unauthorized

            sendResponse(context, status, loggedInAccount);
        }catch(Exception e){
            handleError(context, e);
        }
    }
    

    private void createNewAccount(Context context){
        String jsonAccountInfo = context.body();
        ObjectMapper objM = new ObjectMapper();        // V V V Convert jsonAccountInfo String into Account OBJ
        int status;
        
        try{
            Account account = objM.readValue(jsonAccountInfo, Account.class); 
            Account createdAccount = accountService.createNewAccount(account);             //Call createNewAccount service method
 
            if(createdAccount != null) status = 200;     
            else status = 400; 

            sendResponse(context, status, createdAccount);
        }
        catch(Exception e){
            handleError(context, e);
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