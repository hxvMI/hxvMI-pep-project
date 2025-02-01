package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageServiceImp implements MessageService {

    AccountDAO accountDAO = new AccountDAO();
    MessageDAO messageDAO = new MessageDAO();

    /**
    will not contain a message_id.

    The creation of the message will be successful if and only
     if the message_text is not blank, is not over 255 characters,
     and posted_by refers to a real, existing user. If successful,
      the response body should contain a JSON of the message, 
      including its message_id. The response status should be 200,
       which is the default. The new message should be persisted to the database.
     * @param context
     */

    @Override
    public Message createNewPost(Message message) {
        String messageText = message.getMessage_text();
        if(messageText.isBlank() || messageText.length() > 255) return null;

        boolean existingUser = accountDAO.accountExistsById(message.getPosted_by());
        if(existingUser == false)return null;

        return messageDAO.createNewPost(messageText, message.getPosted_by(), message.getTime_posted_epoch());
    }

}
