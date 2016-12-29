package pricetag;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AwsLambdaProcessor<R> {

    private final Class<R> clazz;
    private final BiFunction<R, Context, Response> process;

    private final static String BASE = "{\n" +
            "    \"statusCode\": 200,\n" +
            "    \"headers\": {\"Content-Type\": \"application/json\"},\n" +
            "    \"body\": \"%s\"\n" +
            "}";

    public AwsLambdaProcessor(BiFunction<R, Context, Response> process, Class<R> clazz) {
        this.clazz = clazz;
        this.process = process;
    }

    public void processAws(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String stringFromInputStream = getStringFromInputStream(inputStream);
        System.out.println("Incoming = " + stringFromInputStream);
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();

        final JsonElement jsonElement = parser.parse(stringFromInputStream);
        final JsonElement body1 = jsonElement.getAsJsonObject().get("body");
        String body = body1.isJsonNull() ? null : body1.getAsString();
        context.getLogger().log("Body = " + body);
        final JsonElement queryStringParameters1 = jsonElement.getAsJsonObject().get("queryStringParameters");
        final JsonObject queryStringParameters = queryStringParameters1.isJsonNull()? null : queryStringParameters1.getAsJsonObject();
        context.getLogger().log("queryStringParameters = " + queryStringParameters);
        R request = body != null ? gson.fromJson(body, clazz) : gson.fromJson(queryStringParameters, clazz);
        context.getLogger().log("Request = " + request.toString());

        final String response = String.format(BASE, process.apply(request, context));
        context.getLogger().log("Response " + response);
        outputStream.write(response.getBytes());
    }

    private static String getStringFromInputStream(InputStream is) {
        return new BufferedReader(new InputStreamReader(is))
                .lines()
                .collect(Collectors.joining());
    }
}