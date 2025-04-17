package bookstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private List<CartItem> items;
    private double totalAmount;
    private Date orderDate;

    public Order() {}

    public Order(int orderId, int customerId, List<CartItem> items, double totalAmount, Date orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    @JsonProperty
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    @JsonProperty
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    @JsonProperty
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    @JsonProperty
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    @JsonProperty
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}