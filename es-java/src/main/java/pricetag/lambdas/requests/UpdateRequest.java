package pricetag.lambdas.requests;

public class UpdateRequest {
    private String productId;
    private Double priceVariation;

    public UpdateRequest() {
    }

    public String getProductId() {
        return productId;
    }

    public Double getPriceVariation() {
        return priceVariation;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "productId='" + productId + '\'' +
                ", priceVariation=" + priceVariation +
                '}';
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setPriceVariation(Double priceVariation) {
        this.priceVariation = priceVariation;
    }
}
