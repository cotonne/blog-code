package pricetag.lambdas.requests;

public class GetRequest {
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "GetRequest{" +
                "productId='" + productId + '\'' +
                '}';
    }

    public String productId;

    public String getProductId() {
        return productId;
    }

    public GetRequest() {

    }
}
