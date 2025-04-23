package bookstore.service;

import bookstore.models.Authors;
import bookstore.models.Books;
import bookstore.models.Cart;
import bookstore.models.Order;
import bookstore.models.Customers;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final List<Books> bookList = new ArrayList<>();
    private static final List<Authors> authorList = new ArrayList<>();
    private static final List<Cart> cartList = new ArrayList<>();
    private static final List<Order> orderList = new ArrayList<>();
    private static final List<Customers> customerList = new ArrayList<>();
    private static int currentBookId = 1; // Separate ID for books
    private static int currentAuthorId = 1; // Separate ID for authors
    private static int currentCustomerId = 1; // Separate ID for customers
    private static int currentOrderId = 1; // Separate ID for orders

    static {
        // Initialize authors
        authorList.add(new Authors(currentAuthorId++, "Paulo Coelho", "Brazilian author known for The Alchemist."));
        authorList.add(new Authors(currentAuthorId++, "George Orwell", "English novelist known for 1984 and Animal Farm."));
        // Initialize books
        bookList.add(new Books(currentBookId++, "The Alchemist", 1, "9780061122415", 1988, 9.99, 20));
        bookList.add(new Books(currentBookId++, "1984", 2, "9780451524935", 1949, 8.99, 15));
        // Initialize customers
        customerList.add(new Customers(currentCustomerId++, "John Doe", "john.doe@example.com", "password123"));
        customerList.add(new Customers(currentCustomerId++, "Jane Smith", "jane.smith@example.com", "password456"));
    }

    public static List<Books> getBookList() {
        return bookList;
    }

    public static List<Authors> getAuthorList() {
        return authorList;
    }

    public static List<Cart> getCartList() {
        return cartList;
    }

    public static List<Order> getOrderList() {
        return orderList;
    }

    public static List<Customers> getCustomerList() {
        return customerList;
    }

    public static int getNextBookId() {
        return currentBookId++;
    }

    public static int getNextAuthorId() {
        return currentAuthorId++;
    }

    public static int getNextCustomerId() {
        return currentCustomerId++;
    }

    public static int getNextOrderId() {
        return currentOrderId++;
    }
}