package bookstore.resource;

import bookstore.models.Books;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private static List<Books> bookList = new ArrayList<>();
    private static int currentId = 1;

    // Static block to add some dummy books
    static {
        bookList.add(new Books(currentId++, "The Alchemist", "Paulo Coelho", "9780061122415", 1988, 9.99, 20));
        bookList.add(new Books(currentId++, "1984", "George Orwell", "9780451524935", 1949, 8.99, 15));
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(bookList).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        for (Books book : bookList) {
            if (book.getId() == id) {
                return Response.ok(book).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
    }

    @POST
    public Response addBook(Books book) {
        book.setId(currentId++);
        bookList.add(book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Books updatedBook) {
        for (Books book : bookList) {
            if (book.getId() == id) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setIsbn(updatedBook.getIsbn());
                book.setPublicationYear(updatedBook.getPublicationYear());
                book.setPrice(updatedBook.getPrice());
                book.setStockQuantity(updatedBook.getStockQuantity());
                return Response.ok(book).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Iterator<Books> iterator = bookList.iterator();
        while (iterator.hasNext()) {
            Books book = iterator.next();
            if (book.getId() == id) {
                iterator.remove();
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
    }
}
