package bookstore.resources;

import bookstore.exceptions.AuthorNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.models.Authors;
import bookstore.models.Books;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static List<Authors> authorList = new ArrayList<>();
    private static List<Books> bookList = new ArrayList<>(); // For /authors/{id}/books
    private static int currentId = 1;

    static {
        authorList.add(new Authors(currentId++, "Paulo Coelho", "Brazilian author known for The Alchemist."));
        authorList.add(new Authors(currentId++, "George Orwell", "English novelist known for 1984 and Animal Farm."));
        bookList.add(new Books(1, "The Alchemist", 1, "9780061122415", 1988, 9.99, 20));
        bookList.add(new Books(2, "1984", 2, "9780451524935", 1949, 8.99, 15));
    }

    @POST
    public Response addAuthor(Authors author) throws InvalidInputException {
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }
        author.setId(currentId++);
        authorList.add(author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Response getAllAuthors() {
        return Response.ok(authorList).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        Authors author = authorList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Authors updatedAuthor) {
        Authors author = authorList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        if (updatedAuthor.getName() != null && !updatedAuthor.getName().isEmpty()) {
            author.setName(updatedAuthor.getName());
        }
        author.setBiography(updatedAuthor.getBiography());
        return Response.ok(author).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Authors author = authorList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        authorList.remove(author);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
        if (!authorList.stream().anyMatch(a -> a.getId() == id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        List<Books> books = bookList.stream()
                .filter(b -> b.getAuthorId() == id)
                .collect(Collectors.toList());
        return Response.ok(books).build();
    }
}