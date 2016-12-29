package pricetag.lambdas.commands;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import pricetag.AwsLambdaProcessor;
import pricetag.Response;
import pricetag.domain.Product;
import pricetag.lambdas.requests.UpdateRequest;
import pricetag.services.Bus;
import pricetag.services.EventStore;
import pricetag.services.ProductRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class UpdateHandler implements RequestStreamHandler {
	private final AwsLambdaProcessor<UpdateRequest> awsLambdaProcessor = new AwsLambdaProcessor<>(this::handleRequest, UpdateRequest.class);

	private ProductRepository productRepository = new ProductRepository(new EventStore(), new Bus());

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        awsLambdaProcessor.processAws(inputStream, outputStream, context);
    }

    public Response handleRequest(UpdateRequest input, Context context) {
		context.getLogger().log("Input " + input);
		Optional<Product> product = productRepository.getById(input.getProductId());
		return product
				.map(p -> updateProduct(p, input.getPriceVariation()))
				.orElse(new Response("Failed to create product, product doesnt exist"));
	}

    private Response updateProduct(Product product, Double priceVariation) {
	    Product newProduct = product.update(priceVariation);
	    productRepository.save(newProduct);
        return new Response("OK");
    }

}
