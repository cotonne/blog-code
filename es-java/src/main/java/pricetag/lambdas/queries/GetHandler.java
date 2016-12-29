package pricetag.lambdas.queries;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import pricetag.AwsLambdaProcessor;
import pricetag.Response;
import pricetag.dao.Database;
import pricetag.lambdas.requests.CreateRequest;
import pricetag.lambdas.requests.GetRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class GetHandler implements RequestStreamHandler {
    private final AwsLambdaProcessor<GetRequest> awsLambdaProcessor = new AwsLambdaProcessor<>(this::handleRequest, GetRequest.class);

    private final static String QUERY = "SELECT price FROM PRICETAG where productId=?";
    private final Database database = new Database();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        awsLambdaProcessor.processAws(inputStream, outputStream, context);
    }

    public Response handleRequest(GetRequest getRequest, Context context) {
        PreparedStatement st = null;
        try {
            Connection conn = database.getConnection();
            st = conn.prepareStatement(QUERY);
            st.setString(1, getRequest.getProductId());
            st.execute();
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return new Response("" + rs.getDouble("price"));
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Response("Failed");

    }
}
