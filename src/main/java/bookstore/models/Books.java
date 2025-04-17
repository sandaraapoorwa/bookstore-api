package bookstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Books {
    private int id;
    private String title;
    private int authorId; // References Authors
    private String isbn;
    private int publicationYear;
    private double price;
    private int stockQuantity;

    public Books() {} // For JSON deserialization

    public Books(int id, String title, int authorId, String isbn, int publicationYear, double price, int stockQuantity) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @JsonProperty
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @JsonProperty
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @JsonProperty
    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    @JsonProperty
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    @JsonProperty
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    @JsonProperty
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @JsonProperty
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}