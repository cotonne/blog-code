package pricetag.lambdas.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
    public Request() {
    }

    private String body;
    private String queryStringParameters;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getQueryStringParameters() {
        return queryStringParameters;
    }

    public void setQueryStringParameters(String queryStringParameters) {
        this.queryStringParameters = queryStringParameters;
    }
}
