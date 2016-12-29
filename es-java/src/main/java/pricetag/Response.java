package pricetag;

public class Response {

    private String message;

    public Response(String message) {
        this.message = message;
    }

    public Response() {
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
