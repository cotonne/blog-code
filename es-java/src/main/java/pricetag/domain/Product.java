package pricetag.domain;

import pricetag.domain.events.Event;
import pricetag.domain.events.ProductCreated;
import pricetag.domain.events.ProductDeleted;
import pricetag.domain.events.ProductUpdated;

import java.util.*;

public class Product {
    private String productId;
    private double price;
    private List<? extends Event> uncommitedEvents;

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", price=" + price +
                '}';
    }

    public Product(String productId, double price) {
        this(productId, price, new ArrayList<>());
    }

    public Product(String productId, double price, List<? extends Event> uncommitedEvents) {
        this.productId = productId;
        this.price = price;
        this.uncommitedEvents = Collections.unmodifiableList(uncommitedEvents);
    }

    public static Product create(String productId, Double price) {
        return new Product(productId, price, Collections.singletonList(new ProductCreated(productId, price)));
    }

    public Product apply(Event event) {
        return this;
    }

    public static Optional<Product> apply(Optional<Product> product, ProductCreated event) {
        return Optional.of(product.orElse(new Product(event.getProductId(), event.getPrice())));
    }

    public static Optional<Product> apply(Optional<Product> product, ProductUpdated event) {
        return product.map(p -> new Product(p.productId, p.price + event.getPriceVariation()));
    }

    public static Optional<Product> apply(Optional<Product> product, ProductDeleted event) {
        return Optional.empty();
    }

    public List<? extends Event> getUncommitedEvents() {
        return uncommitedEvents;
    }

    public Product update(Double priceVariation) {
        List<Event> events = new ArrayList<>(uncommitedEvents);
        events.add(new ProductUpdated(productId, priceVariation));
        return new Product(productId, price + priceVariation, events);
    }
}
