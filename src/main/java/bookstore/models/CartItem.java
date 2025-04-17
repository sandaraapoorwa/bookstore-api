package bookstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {
    private int bookId;
    private int quantity;

    public CartItem() {}

    public CartItem(int bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    @JsonProperty
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    @JsonProperty
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}