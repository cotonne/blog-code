package pricetag;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import pricetag.lambdas.requests.CreateRequest;

import java.io.*;

public class Test implements RequestStreamHandler {

    private final AwsLambdaProcessor<CreateRequest> awsLambdaProcessor = new AwsLambdaProcessor<>(this::response, CreateRequest.class);

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        awsLambdaProcessor.processAws(inputStream, outputStream, context);
    }

    private Response response(CreateRequest request, Context context) {
        context.getLogger().log("Entering in the response function = " + request);
        return new Response("OK");
    }

}
