package bookstore.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customers {
    private int id;
    private String name;
    private String email;
    private String password; // Plain text for simplicity, as per coursework

    public Customers() {}

    public Customers(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @JsonProperty
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @JsonProperty
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @JsonProperty
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}