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

    //Fulde stykliste
    public void calcCarport(Order order) throws DatabaseException {
        calcPost(order);
        calcBeams(order);
        calcRafters(order);
    }

    //Stolper
    private void calcPost(Order order) throws DatabaseException {
        int quantity = calcPostQuantity();
        int price = calcPostPrice();

        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0 ,POST, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Stolper nedgraves 90 cm ned i jorden");
        orderItems.add(orderItem);
    }

    public int calcPostQuantity(){
        return 2 * (2 + (length - 130) / 340);
    }

    public int calcPostPrice() {
        return 0;
    }

    //Remmer
    private void calcBeams(Order order) {
        int quantity = calcBeamQuantity();
        int price = calcBeamPrice();

        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0 ,BEAM, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Remme tekst");
        orderItems.add(orderItem);
    }

    public int calcBeamQuantity() {
        if(length > 600){
            return 4;
        }
        else {
            return 2;}

    }

    public int calcBeamPrice() {
        return 0;
    }

    //Spær
    private void calcRafters(Order order) {
        int quantity = calcRafterQuantity();
        int price = calcRafterPrice();



        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0 ,RAFTER, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Spær tekst");
        orderItems.add(orderItem);
    }

    public int calcRafterQuantity(){
        return (int) Math.round(length / 59.5);
    }

    public int calcRafterPrice() {
        return 0;
    }

    //Styklisten består af alle orderitems i denne liste
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
