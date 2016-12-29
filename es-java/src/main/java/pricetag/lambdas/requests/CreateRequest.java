package pricetag.lambdas.requests;

public class CreateRequest {
    private String productId;
    private Double price;

    public CreateRequest(){

    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "CreateRequest{" +
                "productId='" + productId + '\'' +
                ", price=" + price +
                '}';
    }
}
