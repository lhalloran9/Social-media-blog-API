package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.*;
public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public List<Message> getAllMessagesByUser(int id){
        return messageDAO.getAllMessagesByUser(id);
    }
    public Message getMessagesById(int id){
        return messageDAO.getMessageById(id);
    }
    public Message addMessage(Message message){
        return messageDAO.insertMessage(message);
    }
    public Message deleteMessagesById(int id){
        return messageDAO.deleteMessage(id);
    }
    public Message updateMessageById(int id, String text){
        return messageDAO.updateMessageById(id, text);
    }
    
}
