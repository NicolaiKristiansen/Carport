package app.services;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.ProductVariant;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    //Delete later
    private static final int POST = 1;
    private static final int RAFTER = 2;
    private static final int BEAM = 2;

    public List<OrderItem> orderItems = new ArrayList<>();

    private int width;
    private int length;
    private ConnectionPool connectionPool;

    public Calculator(int width, int length, ConnectionPool connectionPool) {
        this.width = width;
        this.length = length;
        this.connectionPool = connectionPool;
    }

    public void calcCarport(Order order) throws DatabaseException {

        calcPost(order);
        calcBeams(order);
        calcRafters(order);

    }

    //Stolper
    private void calcPost(Order order) throws DatabaseException {
        //Antal stolper
        int quantity = calcPostQuantity();
        //TODO: Make a function for calculating the amount of posts
        //Længde på stopler - dvs variant
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0 ,POST, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Stolper nedgraves 90 cm ned i jorden");
        orderItems.add(orderItem);
    }

    public int calcPostQuantity(){
        int quantity = 2 * (2 + length - 130 / 340);
        return quantity;

    }
    //Remmer
    private void calcBeams(Order order) {

    }
    //Spær
    private void calcRafters(Order order) {

    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
