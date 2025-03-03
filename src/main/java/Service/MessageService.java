package Service;

import java.util.List;

import Model.Message;

public interface MessageService {

    Message createNewPost(Message message);

    List<Message> getAllMessages();

    Message getMessage(int message_id);

    Message deleteMessage(int message_id);

    Message updateMessage(int message_id, Message message);

    List<Message> getAllMessagesByUser(int account_id);
    
}
