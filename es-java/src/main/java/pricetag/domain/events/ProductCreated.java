package pricetag.domain.events;

public class ProductCreated extends Event {

    private final String productId;

    private final double price;

    public ProductCreated(String productId, double price) {
        this.productId = productId;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toJson() {
        return String.format("{\"event\": \"" + EventType.CREATE + "\", \"productId\": \"%s\", \"initialPrice\": \"%s\"}", productId, price);
    }
}
