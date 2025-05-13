package app.persistence;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getAllOrders(User user, ConnectionPool connectionPool) throws DatabaseException {

        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT users.user_id, users.email, users.password, users.role, users. phone, users.address, \n" +
                "order_id, orders.carport_width, orders.carport_length, orders.status, orders.total_price\n" +
                "FROM orders JOIN users ON orders.user_id = users.user_id \n" +
                "WHERE orders.user_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, user.getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");
                User user1 = new User(userId, username, password, role, phone, address);
                Order order = new Order(orderId, status, carportWidth, carportLength, totalPrice, user1);
                orderList.add(order);

            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return orderList;
    }

    public static Order getOrderById(int id, ConnectionPool connectionPool) throws DatabaseException {
        Order order = new Order(1,1,1,1,new User("1", "1", "1", "1", "1"));
        String sql = "SELECT * FROM orders JOIN users ON users.user_id = orders.user_id WHERE orders.order_id = ?";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");
                User user = new User(userId, email, password, role, phone, address);
                order = new Order(orderId, status, carportWidth, carportLength, totalPrice, user);
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }


        public static List<Order> getOrderItemsByOrderID(int id, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE orders.order_id = ? inner join users using(user_id)";
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
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");
                User user = new User(userId, username, password, role, phone, address);
                Order order = new Order(orderId, status, carportWidth, carportLength, totalPrice, user);
                orderList.add(order);

            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return orderList;
    }

    public static void insertOrder(Order order, ConnectionPool connectionPool) throws DatabaseException{
        boolean result = false;
        int newId = 0;
        String sql = "INSERT INTO orders (carport_width, carport_length, status, total_price, user_id) VALUES (?, ?, ?, ?, ?)";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ){
            ps.setInt(1, order.getCarportWidth());
            ps.setInt(2, order.getCarportLength());
            ps.setInt(3, order.getOrderStatusId());
            ps.setInt(4, order.getTotalPrice());
            ps.setInt(5, order.getUser().getUserId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1){
                result = true;
            }

            ResultSet idResultset = ps.getGeneratedKeys();
            if (idResultset.next()){
                newId = idResultset.getInt(1);
                order.setOrderId(newId);
            } else {
                order = null;
            }
        }catch(SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void insertOrderItem(OrderItem orderItem, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "INSERT INTO orders (order_id, product_variant, quantity, description) VALUES (?, ?, ?, ?)";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);

        ){ ps.setInt(1, orderItem.getOrder().getOrderId());
            ps.setInt(2, orderItem.getProductVariant().getProductVariantId());
            ps.setInt(3, orderItem.getQuantity());
            ps.setString(4, orderItem.getDescription());
            ps.executeUpdate();

        }catch(SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static int updateOrder(int newPrice, int orderId, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "UPDATE orders SET total_price = ? WHERE order_id = ?";

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ){
            ps.setInt(1, newPrice);
            ps.setInt(2, orderId);
            int update = ps.executeUpdate();

            if(update == 1){
                System.out.println(update);
                return update;
            } else if (update == 0) {
                System.out.println(update);
                return update;
            }

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return 0;
    }
}

