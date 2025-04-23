package bookstore.resources;

import bookstore.exceptions.AuthorNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.models.Authors;
import bookstore.models.Books;
import bookstore.service.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private static List<Authors> authorList = DataStore.getAuthorList();
    private static List<Books> bookList = DataStore.getBookList();

    @POST
    public Response addAuthor(Authors author) throws InvalidInputException {
        if (author.getName() == null || author.getName().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }
        if (authorList.stream().anyMatch(a -> a.getName().equalsIgnoreCase(author.getName()))) {
            throw new InvalidInputException("Author with name " + author.getName() + " already exists");
        }
        author.setId(DataStore.getNextAuthorId());
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
    public Response updateAuthor(@PathParam("id") int id, Authors updatedAuthor) throws InvalidInputException {
        Authors author = authorList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        if (updatedAuthor.getName() != null && !updatedAuthor.getName().isEmpty()) {
            if (!updatedAuthor.getName().equalsIgnoreCase(author.getName()) &&
                    authorList.stream().anyMatch(a -> a.getName().equalsIgnoreCase(updatedAuthor.getName()))) {
                throw new InvalidInputException("Author with name " + updatedAuthor.getName() + " already exists");
            }
            author.setName(updatedAuthor.getName());
        }
        author.setBiography(updatedAuthor.getBiography());
        return Response.ok(author).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) throws InvalidInputException {
        Authors author = authorList.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
        if (bookList.stream().anyMatch(b -> b.getAuthorId() == id)) {
            throw new InvalidInputException("Cannot delete author with associated books");
        }
        authorList.remove(author);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int id) {
        if (authorList.stream().noneMatch(a -> a.getId() == id)) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found");
        }
        List<Books> books = bookList.stream()
                .filter(b -> b.getAuthorId() == id)
                .collect(Collectors.toList());
        return Response.ok(books).build();
    }
}