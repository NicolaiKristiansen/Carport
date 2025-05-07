package app.persistence;

import app.entities.Product;
import app.entities.ProductVariant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static List<ProductVariant> getVariantByProductIdAndMinLength(int minLength, int productId, ConnectionPool connectionPool){
        List<ProductVariant> productVariants = new ArrayList<>();
        String sql = "SELECT * FROM product_variant" +
                " INNER JOIN product p USING(product_id)" +
                " WHERE product_id = ? AND length >= ?";
        try
                (
                        Connection connection = connectionPool.getConnection()
                ) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, minLength);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int variantId = resultSet.getInt("product_variant_id");
                int product_id = resultSet.getInt("product_id");

                int product_id1 = resultSet.getInt("product_id");
                String productName = resultSet.getString("name");
                String productUnit = resultSet.getString("unit");
                int price = resultSet.getInt("price");

                Product product = new Product(product_id1, productName, productUnit, price);
                ProductVariant productVariant = new ProductVariant(variantId,product, product_id);
                productVariants.add(productVariant);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return productVariants;
    }
}
