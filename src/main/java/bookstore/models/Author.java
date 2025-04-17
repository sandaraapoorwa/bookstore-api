package bookstore.models;

public class Author {
    private int id;
    private String name;

    private String bookName;

    public Author(int id, String name, String bookName) {
        this.id = id;
        this.name = name;
        this.bookName = bookName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
