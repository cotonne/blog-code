package pricetag.lambdas.commands;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import pricetag.AwsLambdaProcessor;
import pricetag.Response;
import pricetag.domain.Product;
import pricetag.lambdas.requests.CreateRequest;
import pricetag.services.Bus;
import pricetag.services.EventStore;
import pricetag.services.ProductRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class CreateHandler implements RequestStreamHandler {
    private final AwsLambdaProcessor<CreateRequest> awsLambdaProcessor = new AwsLambdaProcessor<>(this::handleRequest, CreateRequest.class);

    private ProductRepository productRepository = new ProductRepository(new EventStore(), new Bus());

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        awsLambdaProcessor.processAws(inputStream, outputStream, context);
    }

    public Response handleRequest(CreateRequest input, Context context) {
        context.getLogger().log("Input " + input);
        Optional<Product> product = productRepository.getById(input.getProductId());
        return product
                .map(x -> new Response("Failed to create product, product exists"))
                .orElse(createProduct(input));
    }

    private Response createProduct(CreateRequest input) {
        Product newProduct = Product.create(input.getProductId(), input.getPrice());
        productRepository.save(newProduct);
        return new Response("Go Serverless v1.0! Your function executed successfully!");
    }

}
