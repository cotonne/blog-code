package pricetag.lambdas.requests;

public class ProcessRequest {
    private String event;
    private String productId;
    private Double value;

    @Override
    public String toString() {
        return "ProcessRequest{" +
                "event='" + event + '\'' +
                ", productId='" + productId + '\'' +
                ", value=" + value +
                '}';
    }

    public ProcessRequest() {
    }

    public String getEvent() {
        return event;
    }

    public String getProductId() {
        return productId;
    }

    public Double getValue() {
        return value;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
