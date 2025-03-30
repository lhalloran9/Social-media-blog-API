package DAO;

import Util.ConnectionUtil;
import Model.Account;
import java.sql.*;


public class AccountDAO {
  
    public Account loginAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT account_id, username, password FROM account WHERE username = ?;";
            PreparedStatement s = connection.prepareStatement(sql);
            s.setString(1, account.getUsername());
            ResultSet resultSet = s.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            if(!resultSet.getString("password").equals(account.getPassword()) ){
                return null;
            }
            return new Account(resultSet.getInt("account_id"), resultSet.getString("username"),resultSet.getNString("password"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String checkSql = "SELECT COUNT(*) FROM account WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, account.getUsername());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0){
                return null;
            }
            String sql = "insert into account (username,password) values (?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_author_id, account.getUsername(),account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
