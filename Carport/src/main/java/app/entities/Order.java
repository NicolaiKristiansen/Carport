package app.entities;

import app.persistence.ConnectionPool;
import app.services.SVG;

import java.util.Objects;

public class Order {
    private int orderId;
    private int orderStatusId;
    private int carportWidth;
    private int carportLength;
    private int totalPrice;
    private User user;

    private SVG svg;

    public Order(int orderId, int orderStatusId, int carportWidth, int carportLength, int totalPrice, User user) {
        this.orderId = orderId;
        this.orderStatusId = orderStatusId;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public Order(int orderStatusId, int carportWidth, int carportLength, int totalPrice, User user) {
        this.orderStatusId = orderStatusId;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.totalPrice = totalPrice;
        this.user = user;
    }



    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCarportWidth() {
        return carportWidth;
    }

    public void setCarportWidth(int carportWidth) {
        this.carportWidth = carportWidth;
    }

    public int getCarportLength() {
        return carportLength;
    }

    public void setCarportLength(int carportLength) {
        this.carportLength = carportLength;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public SVG getSvg() {
        return svg;
    }

    public void setSvg(SVG svg) {
        this.svg = svg;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderStatusId=" + orderStatusId +
                ", carportWidth=" + carportWidth +
                ", carportLength=" + carportLength +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId && orderStatusId == order.orderStatusId && carportWidth == order.carportWidth && carportLength == order.carportLength && totalPrice == order.totalPrice && Objects.equals(user, order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderStatusId, carportWidth, carportLength, totalPrice, user);
    }
}
