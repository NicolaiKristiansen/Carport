package app.persistence;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders inner join users using(user_id)";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");
                User user = new User(userId, username, password, role);
                Order order = new Order(orderId, status, carportWidth, carportLength, totalPrice, user);
                orderList.add(order);

            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return orderList;
    }

    //Needs method "getOrderItemsByOrderId"
    public static List<Order> getOrderItemsByOrderID(int id, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE orderId = ? inner join users using(user_id)";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ) {
            preparedStatement.setInt(1, id); //TODO: Make it get orderId when using the function
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");
                User user = new User(userId, username, password, role);
                Order order = new Order(orderId, status, carportWidth, carportLength, totalPrice, user);
                orderList.add(order);

            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return orderList;
    }


    //Needs method "insertOrder"
    public void insertOrder(Order order, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "INSERT INTO orders (carport_width, carport_length, status, user_id) VALUES (?, ?, ?, ?)";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);

        ){ ps.setInt(1, order.getCarportWidth());
            ps.setInt(2, order.getCarportLength());
            ps.setInt(3, order.getOrderStatusId());
            ps.setInt(4, order.getUser().getUserId());
            int updatedTable = ps.executeUpdate();
            if (updatedTable >= 1){
                System.out.println("Inserted Order Successfully");
            } else {
                System.out.println("Inserted Order Failed");
            }

        }catch(SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    //Needs method "insertOrderItems"
    public void insertOrderItem(OrderItem orderItem, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "INSERT INTO orders (order_id, product_variant, quantity, description) VALUES (?, ?, ?, ?)";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);

        ){ ps.setInt(1, ); //TODO: Find how to get order_id
            ps.setInt(2, orderItem.getProductVariant().getProductVariantId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setString(4, orderItem.getDescription());
            int updatedTable = ps.executeUpdate();
            if (updatedTable >= 1){
                System.out.println("Inserted Order Successfully");
            } else {
                System.out.println("Inserted Order Failed");
            }

        }catch(SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}

