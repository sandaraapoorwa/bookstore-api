package bookstore.resources;

import bookstore.exceptions.BookNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.models.Books;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static List<Books> bookList = new ArrayList<>();
    private static int currentId = 1;

    static {
        bookList.add(new Books(currentId++, "The Alchemist", 1, "9780061122415", 1988, 9.99, 20));
        bookList.add(new Books(currentId++, "1984", 2, "9780451524935", 1949, 8.99, 15));
    }

    @POST
    public Response addBook(Books book) throws InvalidInputException {
        if (book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new InvalidInputException("Title and ISBN are required");
        }
        if (book.getPrice() < 0 || book.getStockQuantity() < 0) {
            throw new InvalidInputException("Price and stock quantity must be non-negative");
        }
        book.setId(currentId++);
        bookList.add(book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(bookList).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Books book = bookList.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Books updatedBook) {
        Books book = bookList.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        if (updatedBook.getTitle() != null && !updatedBook.getTitle().isEmpty()) {
            book.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getIsbn() != null && !updatedBook.getIsbn().isEmpty()) {
            book.setIsbn(updatedBook.getIsbn());
        }
        book.setAuthorId(updatedBook.getAuthorId());
        book.setPublicationYear(updatedBook.getPublicationYear());
        book.setPrice(updatedBook.getPrice());
        book.setStockQuantity(updatedBook.getStockQuantity());
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Books book = bookList.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        bookList.remove(book);
        return Response.noContent().build();
    }
}