package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageServiceImp implements MessageService {

    AccountDAO accountDAO = new AccountDAO();
    MessageDAO messageDAO = new MessageDAO();

    @Override
    public Message createNewPost(Message message) {
        String messageText = message.getMessage_text();
        if(messageText.isBlank() || messageText.length() > 255) return null;

        boolean existingUser = accountDAO.accountExistsById(message.getPosted_by());
        if(existingUser == false)return null;

        return messageDAO.createNewPost(messageText, message.getPosted_by(), message.getTime_posted_epoch());
    }

    @Override
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    @Override
    public Message getMessage(int message_id) {
        return messageDAO.getMessage(message_id);
    }

    @Override
    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }


        /**
7: Our API should be able to update a message text identified by a message ID.
As a user, I should be able to submit a PATCH request on the endpoint 
PATCH localhost:8080/messages/{message_id}. 
The request body should contain a new message_text values to 
replace the message identified by message_id. 
The request body can not be guaranteed to contain any other information.

The update of a message should be successful if and only 
if the message id already exists and the new message_text is 
not blank and is not over 255 characters. If the update is successful, 
the response body should contain the full updated message 
(including message_id, posted_by, message_text, and time_posted_epoch), 
and the response status should be 200, which is the default. 
The message existing on the database should have the updated message_text.
If the update of the message is not successful for any reason, 
the response status should be 400. (Client error)
     */
    @Override
    public Message updateMessage(int message_id, Message message) {
        String messageText = message.getMessage_text();
        if(messageText.isBlank() || messageText.length() > 255) return null;

        return messageDAO.updateMessage(message_id, messageText); //other info isn't guarenteed so just pass these 2
    }

}
