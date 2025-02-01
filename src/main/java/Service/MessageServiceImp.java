package Service;

import java.util.ArrayList;
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


    @Override
    public Message updateMessage(int message_id, Message message) {
        String messageText = message.getMessage_text();
        if(messageText.isBlank() || messageText.length() > 255) return null;

        return messageDAO.updateMessage(message_id, messageText); //other info isn't guarenteed so just pass these 2
    }


    @Override
    public List<Message> getAllMessagesByUser(int account_id) {
        if(accountDAO.accountExistsById(account_id) == false)return new ArrayList<>();

        return messageDAO.getAllMessagesByUser(account_id);
    }

}
