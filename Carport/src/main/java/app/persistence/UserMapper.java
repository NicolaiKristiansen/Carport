package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserMapper {

    public static User login(String username, String password, ConnectionPool connectionPool) {
        String sql = "select * from users when username = ? and password = ?";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String existingUsername = resultSet.getString("username");
                if (username.equals(existingUsername)){
                    String existingPassword = resultSet.getString("password");
                    if (password.equals(existingPassword)){
                        System.out.println("Successfully logged on");
                    }
                }

            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<User> getAllUsers(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from users";
        ArrayList<User> users = new ArrayList<>();
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                ){
            while (rs.next()) {
                int userID = rs.getInt("user_id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phonenumber = rs.getString("phone");
                String role = rs.getString("role");
                User user = new User(userID, email, password, role);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return users;
    }
    //It worked

    public void insertUser(User user, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, password, phone, role) VALUES (?, ?, ?, ?)";
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);

                ){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
           // ps.setString(3, ); //TODO: We need phone number
            ps.setString(4, user.getRole());
            int affectedTable = ps.executeUpdate();
            if (affectedTable > 0){
                System.out.println("User " + user.getUsername() + " inserted successfully");
            } else {
                System.out.println("User " + user.getUsername() + " could not be inserted");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void deleteUser(User user, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ){
            ps.setInt(1, user.getUserId());
            int affectedTable = ps.executeUpdate();
            if (affectedTable > 0){
                System.out.println("User " + user.getUsername() + " deleted successfully");
            } else {
                System.out.println("User " + user.getUsername() + " could not be deleted");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
