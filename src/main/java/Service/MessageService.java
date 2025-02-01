package Service;

import java.util.List;

import Model.Message;

public interface MessageService {

    Message createNewPost(Message message);

    List<Message> getAllMessages();

    Message getMessage(int message_id);
    
}
