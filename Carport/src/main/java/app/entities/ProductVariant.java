package app.entities;

public class ProductVariant {
    private int productVariantId;
    private Product product;
    private int length;

    public ProductVariant(Product product, int productVariantId, int length) {
        this.product = product;
        this.productVariantId = productVariantId;
        this.length = length;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "ProductVariant{" +
                "productVariantId=" + productVariantId +
                ", product=" + product +
                ", length=" + length +
                '}';
    }
}
