package pricetag.lambdas.requests;

public class DeleteRequest {
    private String productId;

    public DeleteRequest() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "DeleteRequest{" +
                "productId='" + productId + '\'' +
                '}';
    }
}
