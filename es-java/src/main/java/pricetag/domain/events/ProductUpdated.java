package pricetag.domain.events;

public class ProductUpdated extends Event {
    private final String productId;
    private final double priceVariation;

    public String getProductId() {
        return productId;
    }

    public double getPriceVariation() {
        return priceVariation;
    }

    public ProductUpdated(String productId, double priceVariation) {
        this.productId = productId;
        this.priceVariation = priceVariation;
    }

    @Override
    public String toJson() {
        return String.format("{\"event\": \"" + EventType.UPDATE + "\", \"productId\": \"%s\", \"priceVariation\": \"%s\"}", productId, priceVariation);
    }
}
