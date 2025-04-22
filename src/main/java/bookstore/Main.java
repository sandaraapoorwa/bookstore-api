package bookstore;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.io.IOException;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/api/";

    public static HttpServer startServer() {
        final ResourceConfig config = new ResourceConfig()
                .packages("bookstore.resources", "bookstore.exceptions");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("Bookstore API started at " + BASE_URI);
        System.out.println("Available endpoints:");
        System.out.println("- /books (GET, POST, PUT, DELETE)");
        System.out.println("- /authors (GET, POST, PUT, DELETE, GET /authors/{id}/books)");
        System.out.println("- /customers (GET, POST, PUT, DELETE)");
        System.out.println("- /customers/{customerId}/cart (GET, POST /items, PUT /items/{bookId}, DELETE /items/{bookId})");
        System.out.println("- /customers/{customerId}/orders (GET, POST, GET /{orderId})");
        System.out.println("Press enter to stop...");
        System.in.read();
        server.shutdownNow();
    }
}