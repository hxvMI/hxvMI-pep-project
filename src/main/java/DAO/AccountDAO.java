package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    Connection conn = ConnectionUtil.getConnection();

    public boolean isUsernameAvailable(String username){
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
                    return new Account(id, username,password);
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


    
}
