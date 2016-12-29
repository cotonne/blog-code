package pricetag.services;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import org.joda.time.DateTime;
import pricetag.domain.events.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static pricetag.domain.events.EventType.*;

public class EventStore {
    public List<Event> getEventsFromAggregate(String productId) {
        System.out.println("Events from aggregate : " + productId);
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#id", "productId");

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":yyyy", productId);

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#id = :yyyy")
                .withNameMap(nameMap)
                .withValueMap(valueMap);
        //  TODO : order by date?

        ItemCollection<QueryOutcome> items = table().query(querySpec);
        List<Event> events = new ArrayList<>();
        items.forEach(item -> events.add(read(item)));
        System.out.println("Number of events retrieved : " + events.size());
        return events;
    }

    private Event read(Item item) {
        EventType e = valueOf(item.getString("event"));

        switch (e) {
            case CREATE:
                return new ProductCreated(item.getString("productId"), item.getFloat("price"));
            case UPDATE:
                return new ProductUpdated(item.getString("productId"), item.getFloat("priceVariation"));
            case DELETE:
                return new ProductDeleted(item.getString("productId"));
        }
        return null;
    }

    public void save(Event event) {
        if (event instanceof ProductCreated) {
            ProductCreated productCreated = (ProductCreated) event;
            table()
                    .putItem(new Item()
                            .withPrimaryKey("productId", productCreated.getProductId(), "date", DateTime.now().getMillis())
                            .withString("event", CREATE.toString())
                            .withDouble("price", productCreated.getPrice())
                            .withBinary("data", event.toJson().getBytes()));
        } else if (event instanceof ProductUpdated) {
            ProductUpdated productUpdated = (ProductUpdated) event;
            table()
                    .putItem(new Item()
                            .withPrimaryKey("productId", productUpdated.getProductId(), "date", DateTime.now().getMillis())
                            .withString("event", UPDATE.toString())
                            .withDouble("priceVariation", productUpdated.getPriceVariation())
                            .withBinary("data", event.toJson().getBytes()));
        } else if (event instanceof ProductDeleted) {
            ProductDeleted productDeleted = (ProductDeleted) event;
            table()
                    .putItem(new Item()
                            .withPrimaryKey("productId", productDeleted.getProductId(), "date", DateTime.now().getMillis())
                            .withString("event", DELETE.toString())
                            .withBinary("data", event.toJson().getBytes()));
        }
    }

    private Table table() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient()
                .withRegion(Regions.US_EAST_1);

        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB.getTable("product-events");
    }

}
