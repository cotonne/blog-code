package pricetag.services;

import pricetag.domain.Product;
import pricetag.domain.events.Event;
import pricetag.domain.events.ProductCreated;

import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final EventStore eventStore;
    private final Bus bus;

    public ProductRepository(EventStore eventStore, Bus bus) {
        this.eventStore = eventStore;
        this.bus = bus;
    }

    public Optional<Product> getById(String productId) {
        List<Event> events = eventStore.getEventsFromAggregate(productId);
        final Optional<Product> product = reapplyEvents(events);
        System.out.println("Final product = " + product);
        return product;
    }

    public void save(Product product) {
        product.getUncommitedEvents().forEach(eventStore::save);
        product.getUncommitedEvents().forEach(bus::publish);
    }

    private Optional<Product> reapplyEvents(List<Event> events) {
        return events.stream().reduce(Optional.empty(),
                this::accumulator,
                (currentState, otherState) -> otherState);
    }

    private Optional<Product> accumulator(Optional<Product> product, Event event) {
        if(!product.isPresent() && event instanceof ProductCreated) {
            ProductCreated productCreated = (ProductCreated)event;
            return Optional.of(new Product(productCreated.getProductId(), productCreated.getPrice()));
        }
        return product.map(p -> p.apply(event));
    }
}
