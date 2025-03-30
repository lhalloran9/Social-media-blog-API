package DAO;
import java.sql.Connection;
import Model.Message;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Util.ConnectionUtil;
import java.sql.Statement;

public class MessageDAO {
    
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            
            String sql = "select * from message where message_id = ?;"; 
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),
                rs.getInt("time_posted_epoch"));
            }
            else{
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String checkSql = "SELECT FROM message WHERE posted_by = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setInt(1, message.getPosted_by());
            ResultSet resultSet = checkStatement.executeQuery();
            if (!resultSet.next()) {
                return null;}
            String sql = "insert into message (posted_by,message_text, time_posted_epoch) values(?,?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,message.getPosted_by() );
            preparedStatement.setString(2,message.getMessage_text() );
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            String s = "select * from message where message_id = ?;";
            PreparedStatement pre = connection.prepareStatement(s);
            pre.setInt(1,id);
            ResultSet rs = pre.executeQuery();
            if(rs.next()){
                message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));    
            }
            else{
                return null;
            }  
                String sql = "delete from message where message_id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,id);
                preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message updateMessageById(int id, String text){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
    try {
        String checkSql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkSql);
        checkStatement.setInt(1, id);
        ResultSet resultSet = checkStatement.executeQuery();
        if (!resultSet.next()) {
            return null; 
        }
        int postedBy = resultSet.getInt("posted_by");
        long timePostedEpoch = resultSet.getLong("time_posted_epoch");
        String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
        updateStatement.setString(1, text);
        updateStatement.setInt(2, id);
        int rowsUpdated = updateStatement.executeUpdate();
        if (rowsUpdated > 0) {
            message = new Message(id, postedBy, text, timePostedEpoch);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return message;
    }

    public List<Message> getAllMessagesByUser(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message where posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),
                rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

   
}
