package bookstore.resources;

import bookstore.exceptions.BookNotFoundException;
import bookstore.exceptions.CartNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.exceptions.OutOfStockException;
import bookstore.models.Books;
import bookstore.models.Cart;
import bookstore.models.CartItem;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private static List<Cart> cartList = new ArrayList<>();
    private static List<Books> bookList = new ArrayList<>();

    static {
        bookList.add(new Books(1, "The Alchemist", 1, "9780061122415", 1988, 9.99, 20));
        bookList.add(new Books(2, "1984", 2, "9780451524935", 1949, 8.99, 15));
    }

    @GET
    public Response getCart(@PathParam("customerId") int customerId) {
        Cart cart = cartList.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart(customerId);
                    cartList.add(newCart);
                    return newCart;
                });
        return Response.ok(cart).build();
    }

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) throws InvalidInputException {
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be positive");
        }
        Books book = bookList.stream()
                .filter(b -> b.getId() == item.getBookId())
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + item.getBookId() + " not found"));
        if (book.getStockQuantity() < item.getQuantity()) {
            throw new OutOfStockException("Insufficient stock for book ID " + item.getBookId());
        }

        Cart cart = cartList.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart(customerId);
                    cartList.add(newCart);
                    return newCart;
                });

        cart.addItem(item);
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateItemQuantity(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, CartItem item) throws InvalidInputException {
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be positive");
        }
        Cart cart = cartList.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException("Cart for customer ID " + customerId + " not found"));
        Books book = bookList.stream()
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));
        if (book.getStockQuantity() < item.getQuantity()) {
            throw new OutOfStockException("Insufficient stock for book ID " + bookId);
        }

        cart.updateItemQuantity(bookId, item.getQuantity());
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        Cart cart = cartList.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException("Cart for customer ID " + customerId + " not found"));
        cart.removeItem(bookId);
        return Response.ok(cart).build();
    }
}