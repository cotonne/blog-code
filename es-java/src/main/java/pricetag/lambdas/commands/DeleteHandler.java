package pricetag.lambdas.commands;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import pricetag.AwsLambdaProcessor;
import pricetag.Response;
import pricetag.lambdas.requests.CreateRequest;
import pricetag.lambdas.requests.DeleteRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DeleteHandler implements RequestStreamHandler {
	private final AwsLambdaProcessor<DeleteRequest> awsLambdaProcessor = new AwsLambdaProcessor<>(this::handleRequest, DeleteRequest.class);

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        awsLambdaProcessor.processAws(inputStream, outputStream, context);
    }

	public Response handleRequest(DeleteRequest input, Context context) {
        // TODO : Implement me
		return new Response("Go Serverless v1.0! Your function executed successfully!");
	}

}
