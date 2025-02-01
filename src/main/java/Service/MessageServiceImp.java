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

}
