package app.entities;

public class CustomerOrders {

    private double length;
    private double width;
    private int id;
    private double price;
    private int status;

    public CustomerOrders(double length, double width, int id, int price, int status) {
        this.length = length;
        this.width = width;
        this.id = id;
        this.price = price;
        this.status = status;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomerOrders{" +
                "length=" + length +
                ", width=" + width +
                ", id=" + id +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
