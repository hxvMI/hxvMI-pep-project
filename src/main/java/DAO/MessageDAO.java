package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import Util.ConnectionUtil;

public class MessageDAO {


    static Connection conn = ConnectionUtil.getConnection();

    //create new message and return all its info
    /**
     * create table message (
    message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)
);
     */
    public Message createNewPost(String message_text, int posted_by, long time_posted_epoch) {
        String query = "INSERT INTO message(message_text, posted_by, time_posted_epoch) VALUES(?, ?, ?)";
        int rowsAffected = 0;

        
        try{
            PreparedStatement pStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, message_text);
            pStatement.setInt(2, posted_by);
            pStatement.setLong(3, time_posted_epoch);

            rowsAffected = pStatement.executeUpdate();

            //is true if more than 0 rowsAffected
            if(rowsAffected > 0){
                ResultSet res = pStatement.getGeneratedKeys();

                if(res.next()){
                    int message_id = res.getInt("message_id");
                    return new Message(message_id, posted_by, message_text, time_posted_epoch);
                }
            }   
        }
        catch(SQLException e){
            System.out.println("Error in AccountDAO createNewPost");
            e.printStackTrace(); // Log the error for debugging
            return null;
        }

        return null;
    }

}
