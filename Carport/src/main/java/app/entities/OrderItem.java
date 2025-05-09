package app.entities;

import java.util.Objects;

public class OrderItem {
    private int orderItemId;
    private Order order;
    private ProductVariant productVariant;
    private int quantity;
    private String description;

    public OrderItem(int orderItemId, Order order, ProductVariant productVariant, int quantity, String description) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.description = description;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", order=" + order +
                ", productVariant=" + productVariant +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return orderItemId == orderItem.orderItemId && quantity == orderItem.quantity && Objects.equals(order, orderItem.order) && Objects.equals(productVariant, orderItem.productVariant) && Objects.equals(description, orderItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, order, productVariant, quantity, description);
    }
}
