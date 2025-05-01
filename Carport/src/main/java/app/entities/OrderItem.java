package app.entities;

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
}
