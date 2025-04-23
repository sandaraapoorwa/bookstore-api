package bookstore.resources;

import bookstore.exceptions.BookNotFoundException;
import bookstore.exceptions.CartNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.exceptions.OutOfStockException;
import bookstore.models.Books;
import bookstore.models.Cart;
import bookstore.models.CartItem;
import bookstore.models.Order;
import bookstore.service.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static List<Order> orderList = DataStore.getOrderList();
    private static List<Cart> cartList = DataStore.getCartList();
    private static List<Books> bookList = DataStore.getBookList();

    @POST
    public Response createOrder(@PathParam("customerId") int customerId) throws InvalidInputException {
        Cart cart = cartList.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException("Cart for customer ID " + customerId + " not found"));
        if (cart.getItems().isEmpty()) {
            throw new InvalidInputException("Cart is empty");
        }

        double totalAmount = 0.0;
        for (CartItem item : cart.getItems()) {
            Books book = bookList.stream()
                    .filter(b -> b.getId() == item.getBookId())
                    .findFirst()
                    .orElseThrow(() -> new BookNotFoundException("Book with ID " + item.getBookId() + " not found"));
            if (book.getStockQuantity() < item.getQuantity()) {
                throw new OutOfStockException("Insufficient stock for book ID " + item.getBookId());
            }
            totalAmount += book.getPrice() * item.getQuantity();
        }

        for (CartItem item : cart.getItems()) {
            Books book = bookList.stream()
                    .filter(b -> b.getId() == item.getBookId())
                    .findFirst()
                    .get();
            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
        }

        Order order = new Order(DataStore.getNextOrderId(), customerId, new ArrayList<>(cart.getItems()), totalAmount, new Date());
        orderList.add(order);
        cart.getItems().clear();

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getOrdersByCustomer(@PathParam("customerId") int customerId) {
        List<Order> customerOrders = orderList.stream()
                .filter(o -> o.getCustomerId() == customerId)
                .collect(Collectors.toList());
        return Response.ok(customerOrders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) throws InvalidInputException {
        Order order = orderList.stream()
                .filter(o -> o.getOrderId() == orderId && o.getCustomerId() == customerId)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Order with ID " + orderId + " not found for customer ID " + customerId));
        return Response.ok(order).build();
    }
}