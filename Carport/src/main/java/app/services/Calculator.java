package app.services;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.Product;
import app.entities.ProductVariant;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.ProductMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private static final int POST = 11;
    private static final int RAFTER = 8;
    private static final int BEAM = 8;

    public List<OrderItem> orderItems = new ArrayList<>();
    public int totalPrice;

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
        order.setTotalPrice(totalPrice);
    }

    private void calcPost(Order order) throws DatabaseException {
        int quantity = calcPostQuantity();
        totalPrice += calcPostPrice();
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0 ,POST, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Stolper nedgraves 90 cm ned i jorden");
        orderItems.add(orderItem);
    }

    public int calcPostQuantity(){
        return 2 * (2 + (length - 130) / 340);
    }

    public int calcPostPrice() throws DatabaseException {
        int quantity = calcPostQuantity();
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0, POST, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        Product product = productVariant.getProduct();
        int pricePerPost = product.getPrice();
        int productLength = productVariant.getLength();
        return (productLength * quantity) * pricePerPost;
    }

    private void calcBeams(Order order) {
        int quantity = calcBeamQuantity();
        totalPrice += calcBeamPrice();


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
        int quantity = calcBeamQuantity();
        int minLength = 0;
        if(quantity > 3) {
            minLength = 500;
        }
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(minLength, BEAM, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        Product product = productVariant.getProduct();
        int pricePerBeam = product.getPrice();
        int productLength = productVariant.getLength();
        return (productLength * quantity) * pricePerBeam;
    }

    private void calcRafters(Order order) {
        int quantity = calcRafterQuantity();
        totalPrice += calcRafterPrice();

        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(0 ,RAFTER, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        OrderItem orderItem = new OrderItem(0, order, productVariant, quantity, "Spær tekst");
        orderItems.add(orderItem);
    }

    public int calcRafterQuantity(){
        return (int) Math.round(length / 59.5);
    }

    public int calcRafterPrice() {
        int quantity = calcRafterQuantity();
        int minLength = 0;
        if (length > 500) {
            minLength = 500;
        }
        List<ProductVariant> productVariants = ProductMapper.getVariantByProductIdAndMinLength(minLength, RAFTER, connectionPool);
        ProductVariant productVariant = productVariants.get(0);
        Product product = productVariant.getProduct();
        int pricePerRafter = product.getPrice();
        int productLength = productVariant.getLength();
        return (productLength * quantity) * pricePerRafter;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
