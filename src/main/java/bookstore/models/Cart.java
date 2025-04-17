package bookstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int customerId;
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(int customerId) {
        this.customerId = customerId;
    }

    @JsonProperty
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    @JsonProperty
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.getBookId() == item.getBookId()) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(int bookId) {
        items.removeIf(item -> item.getBookId() == bookId);
    }

    public void updateItemQuantity(int bookId, int quantity) {
        for (CartItem item : items) {
            if (item.getBookId() == bookId) {
                item.setQuantity(quantity);
                return;
            }
        }
    }
}