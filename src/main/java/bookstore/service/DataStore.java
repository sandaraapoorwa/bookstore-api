package bookstore.service;

import bookstore.models.Authors;
import bookstore.models.Books;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final List<Books> bookList = new ArrayList<>();
    private static final List<Authors> authorList = new ArrayList<>();
    private static int currentId = 1;

    static {
        // Initialize authors
        authorList.add(new Authors(currentId++, "Paulo Coelho", "Brazilian author known for The Alchemist."));
        authorList.add(new Authors(currentId++, "George Orwell", "English novelist known for 1984 and Animal Farm."));
        // Initialize books
        bookList.add(new Books(1, "The Alchemist", 1, "9780061122415", 1988, 9.99, 20));
        bookList.add(new Books(2, "1984", 2, "9780451524935", 1949, 8.99, 15));
    }
    public static List<Books> getBookList() {
        return bookList;
    }
    public static List<Authors> getAuthorList() {
        return authorList;
    }

    public static int getNextId() {
        return currentId++;
    }
}