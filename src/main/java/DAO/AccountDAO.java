package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    Connection conn = ConnectionUtil.getConnection();

    public boolean isUsernameAvailable(String username){ //remake later maybe
        String query = "SELECT COUNT(username) AS count FROM account WHERE username = ?;";

        try{
            PreparedStatement pStatement = conn.prepareStatement(query);
            pStatement.setString(1, username);

            ResultSet res = pStatement.executeQuery();
            res.next();
            int count = res.getInt("count");

            return count == 0; // Returns true if username is free
        }
        catch(SQLException e){
            System.out.println("Error in AccountDAO hasDupUsername");
            e.printStackTrace(); // Log the error for debugging
            return false;
        }
    }



    public Account createNewAccount(String username, String password) {

        //Check if name avalible
        if(isUsernameAvailable(username) == false)return null;


        String query = "INSERT INTO account(username, password) VALUES(?, ?);";
        int rowsAffected = 0;

        
        try{
            PreparedStatement pStatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, username);
            pStatement.setString(2, password);

            rowsAffected = pStatement.executeUpdate();

            //is true if more than 0 rowsAffected
            if(rowsAffected > 0){
                ResultSet res = pStatement.getGeneratedKeys();

                if(res.next()){
                    int id = res.getInt(1);
                    return new Account(id, username, password);
                }
            }   
        }
        catch(SQLException e){
            System.out.println("Error in AccountDAO createNewAccount");
            e.printStackTrace(); // Log the error for debugging
            return null;
        }
        
        return null;
    }



    public Account login(String username, String password) {
        String sql = "SELECT * FROM account where username = ? AND password = ?";

        try{
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, username);
            pStatement.setString(2, password);

            ResultSet res = pStatement.executeQuery();


            if(res.next()){ //1st if START      //will be no .next() if nothing found
                int id = res.getInt("account_id");
                return new Account(id, username, password);
            }//1st if END
        }
        catch(SQLException e){
            System.out.println("Error in AccountDAO login");
            e.printStackTrace(); // Log the error for debugging
            return null;
        }    
    
        return null;
    }


    //message_id is the same as account_id
    public boolean accountExistsById(int account_id) {
        String query = "SELECT * FROM account WHERE account_id = ?;";

        try{
            PreparedStatement pStatement = conn.prepareStatement(query);
            pStatement.setInt(1, account_id);


            ResultSet res = pStatement.executeQuery();
            if (res.next()) return true;    //if there is a next row means an account with that id exists so true
        }
        catch(SQLException e){
            System.out.println("Error in AccountDAO hasDupUsername");
            e.printStackTrace(); // Log the error for debugging
            return false;
        }    

        return false;
    }


    
}
