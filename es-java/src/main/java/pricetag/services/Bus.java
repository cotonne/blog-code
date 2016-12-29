package pricetag.services;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import pricetag.domain.events.Event;

public class Bus {

    private final String arn = System.getenv("arn");

    private AmazonSNSClient getBus() {
        return new AmazonSNSClient()
                .withRegion(Region.getRegion(Regions.US_EAST_1));
    }

    public void publish(Event event) {
        AmazonSNSClient bus = getBus();
        String msg = event.toJson();
        System.out.printf("Event : " + msg);
        bus.publish(new PublishRequest(arn, msg));
    }
}
