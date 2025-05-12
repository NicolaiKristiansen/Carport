package app.persistence;

import app.PasswordUtil;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;

public class UserMapper {


    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String userEmail = resultSet.getString("email");
                String hashedPassword = resultSet.getString("password");
                String userRole = resultSet.getString("role");
                String userPhone = resultSet.getString("phone");
                String userAddress = resultSet.getString("address");

                if (PasswordUtil.checkPassword(password, hashedPassword)) {
                    System.out.println("Login system works");
                    return new User(id, userEmail, hashedPassword, userRole, userPhone, userAddress);
                } else {
                    throw new DatabaseException("Incorrect password");
                }
            } else {
                throw new DatabaseException("User not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
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

    public static void insertUser(User user, ConnectionPool connectionPool) throws DatabaseException {
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
