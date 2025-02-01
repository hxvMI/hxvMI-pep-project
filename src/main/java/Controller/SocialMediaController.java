package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import Service.AccountServiceImp;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService = new AccountServiceImp();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
  
//    The body will contain a representation of a JSON Account, but will not contain an account_id.
//    this:: passses the Context obj
        app.post("register", this::createNewAccount);
        app.post("login", this::login);


        return app;
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