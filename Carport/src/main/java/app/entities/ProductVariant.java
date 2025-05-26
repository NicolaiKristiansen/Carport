package app.entities;

import java.util.Objects;

public class ProductVariant {
    private int productVariantId;
    private Product product;
    private int length;

    public ProductVariant(int productVariantId,Product product, int length) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariant that = (ProductVariant) o;
        return productVariantId == that.productVariantId && length == that.length && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productVariantId, product, length);
    }
}
