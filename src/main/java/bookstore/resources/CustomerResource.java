package bookstore.resources;

import bookstore.exceptions.CustomerNotFoundException;
import bookstore.exceptions.InvalidInputException;
import bookstore.models.Customers;
import bookstore.service.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static List<Customers> customerList = DataStore.getCustomerList();

    @POST
    public Response addCustomer(Customers customer) throws InvalidInputException {
        if (customer.getName() == null || customer.getName().isEmpty() ||
                customer.getEmail() == null || customer.getEmail().isEmpty() ||
                customer.getPassword() == null || customer.getPassword().isEmpty()) {
            throw new InvalidInputException("Name, email, and password are required");
        }
        if (customerList.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(customer.getEmail()))) {
            throw new InvalidInputException("Email already registered");
        }
        customer.setId(DataStore.getNextCustomerId());
        customerList.add(customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Response getAllCustomers() {
        return Response.ok(customerList).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customers customer = customerList.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customers updatedCustomer) throws InvalidInputException {
        Customers customer = customerList.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
        if (updatedCustomer.getName() != null && !updatedCustomer.getName().isEmpty()) {
            customer.setName(updatedCustomer.getName());
        }
        if (updatedCustomer.getEmail() != null && !updatedCustomer.getEmail().isEmpty()) {
            if (!updatedCustomer.getEmail().equalsIgnoreCase(customer.getEmail()) &&
                    customerList.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(updatedCustomer.getEmail()))) {
                throw new InvalidInputException("Email " + updatedCustomer.getEmail() + " already registered");
            }
            customer.setEmail(updatedCustomer.getEmail());
        }
        if (updatedCustomer.getPassword() != null && !updatedCustomer.getPassword().isEmpty()) {
            customer.setPassword(updatedCustomer.getPassword());
        }
        return Response.ok(customer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customers customer = customerList.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
        customerList.remove(customer);
        return Response.noContent().build();
    }
}