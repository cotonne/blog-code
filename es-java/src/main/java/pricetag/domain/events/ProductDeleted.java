package pricetag.domain.events;

public class ProductDeleted extends Event {
    private final String productId;

    public String getProductId() {
        return productId;
    }

    public ProductDeleted(String productId) {
        this.productId = productId;
    }

    @Override
    public String toJson() {
        return String.format("{\"event\": \"" + EventType.DELETE + "\", \"productId\": \"%s\"}", productId);
    }
}
