package pricetag.lambdas.projections;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pricetag.Response;
import pricetag.domain.events.EventType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProcessHandler implements RequestHandler<SNSEvent, Response> {

    static {
        try {
            System.out.println("Registering driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Registering driver done!");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final static String CREATE_QUERY = "INSERT INTO PRICETAG(productId, price) values(?, ?)";
    private final static String UPDATE_QUERY = "UPDATE PRICETAG set price=(price + ?) where productId=?";
    private final static String DELETE_QUERY = "DELETE FROM PRICETAG where productId=?";

    @Override
    public Response handleRequest(SNSEvent event, Context context) {
        final List<SNSEvent.SNSRecord> records = event.getRecords();
        records.forEach(record -> {
                    final String message = record.getSNS().getMessage();
                    System.out.println("New event to process : " + message);
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(message);
                    final JsonObject asJsonObject = element.getAsJsonObject();
                    final String asString = asJsonObject.get("event").getAsString();
                    final String productId = asJsonObject.get("productId").getAsString();
                    EventType eventType = EventType.valueOf(asString);
                    switch (eventType) {
                        case CREATE:
                            create(productId, asJsonObject.get("initialPrice").getAsDouble());
                            break;
                        case UPDATE:
                            update(productId, asJsonObject.get("priceVariation").getAsDouble());
                            break;
                        case DELETE:
                            delete(productId);
                            break;

                    }
                }
        );
        return new Response("OK");
    }

    private void delete(String productId) {
        System.out.println("ProcessHandler.delete");
        System.out.println("productId = [" + productId + "]");
        PreparedStatement st = null;
        try {
            Connection conn = getConnection();
            st = conn.prepareStatement(DELETE_QUERY);
            st.setString(1, productId);
            st.execute();
            System.out.println("Done with delete");
        } catch (Exception e) {
            System.out.println("Got an exception! ");
            System.out.println(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void update(String productId, Double value) {
        System.out.println("ProcessHandler.update");
        System.out.println("productId = [" + productId + "], value = [" + value + "]");
        PreparedStatement st = null;
        try {
            Connection conn = getConnection();
            st = conn.prepareStatement(UPDATE_QUERY);
            st.setDouble(1, value);
            st.setString(2, productId);
            st.execute();
            System.out.println("Done with update");
        } catch (Exception e) {
            System.out.println("Got an exception! ");
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void create(String productId, Double value) {
        System.out.println("ProcessHandler.create!");
        System.out.println("productId = [" + productId + "], value = [" + value + "]");
        PreparedStatement st = null;
        try {
            Connection conn = getConnection();
            System.out.println("Opening connection done");
            st = conn.prepareStatement(CREATE_QUERY);
            System.out.println("Prepare statement done");
            st.setString(1, productId);
            st.setDouble(2, value);
            System.out.println("Before execute");
            st.execute();
            System.out.println("Done with create");
        } catch (Exception e) {
            System.out.println("Got an exception! ");
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Connection getConnection() {
        final String myUrl = System.getenv("databaseUrl");
        final String user = System.getenv("databaseUser");
        final String password = System.getenv("databasePassword");
        System.out.println("getConnection : " + myUrl);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(myUrl, user, password);
        } catch (SQLException e) {
            System.out.println("Failure!");
            e.printStackTrace();
        }
        System.out.println("getConnection done");
        return connection;
    }
}
