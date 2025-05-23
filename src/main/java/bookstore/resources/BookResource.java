package bookstore.resources;

import bookstore.exceptions.BookNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.models.Books;
import bookstore.service.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private static List<Books> bookList = DataStore.getBookList();

    @POST
    public Response addBook(Books book) throws InvalidInputException {
        if (book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new InvalidInputException("Title and ISBN are required");
        }
        if (bookList.stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()))) {
            throw new InvalidInputException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        if (book.getPrice() < 0 || book.getStockQuantity() < 0) {
            throw new InvalidInputException("Price and stock quantity must be non-negative");
        }
        if (DataStore.getAuthorList().stream().noneMatch(a -> a.getId() == book.getAuthorId())) {
            throw new InvalidInputException("Author with ID " + book.getAuthorId() + " does not exist");
        }
        book.setId(DataStore.getNextBookId());
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
    public Response updateBook(@PathParam("id") int id, Books updatedBook) throws InvalidInputException {
        Books book = bookList.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        if (updatedBook.getTitle() != null && !updatedBook.getTitle().isEmpty()) {
            book.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getIsbn() != null && !updatedBook.getIsbn().isEmpty()) {
            if (!updatedBook.getIsbn().equals(book.getIsbn()) &&
                    bookList.stream().anyMatch(b -> b.getIsbn().equals(updatedBook.getIsbn()))) {
                throw new InvalidInputException("Book with ISBN " + updatedBook.getIsbn() + " already exists");
            }
            book.setIsbn(updatedBook.getIsbn());
        }
        if (updatedBook.getAuthorId() > 0 && DataStore.getAuthorList().stream().noneMatch(a -> a.getId() == updatedBook.getAuthorId())) {
            throw new InvalidInputException("Author with ID " + updatedBook.getAuthorId() + " does not exist");
        }
        if (updatedBook.getAuthorId() > 0) {
            book.setAuthorId(updatedBook.getAuthorId());
        }
        book.setPublicationYear(updatedBook.getPublicationYear());
        if (updatedBook.getPrice() >= 0) {
            book.setPrice(updatedBook.getPrice());
        }
        if (updatedBook.getStockQuantity() >= 0) {
            book.setStockQuantity(updatedBook.getStockQuantity());
        }
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