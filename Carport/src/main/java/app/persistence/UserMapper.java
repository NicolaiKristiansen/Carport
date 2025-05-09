package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;

public class UserMapper {


    public static User login(String username, String password, ConnectionPool connectionPool) {
        String sql = "select * from users WHERE email = ? and password = ?";

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
        return null;
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
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                User user = new User(userID, email, password, role, phone, address);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return users;
    }
    //It worked

    public User insertUser(User user, ConnectionPool connectionPool) throws DatabaseException {
        boolean result = false;
        int newId = 0;
        String sql = "INSERT INTO users (email, password, role, phone, address) VALUES (?, ?, ?, ?, ?)";
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ){
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1){
                result = true;
            }
            ResultSet idResultset = ps.getGeneratedKeys();
            if (idResultset.next()){
                newId = idResultset.getInt(1);
                user.setUserId(newId);
            } else {
                user = null;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return user;
    }

    public boolean deleteUser(int userId, ConnectionPool connectionPool) throws DatabaseException, SQLException {
        boolean result = false;
        String sql = "DELETE FROM users WHERE user_id = ?";

        try(
                Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    result = true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
