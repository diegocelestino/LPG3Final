package com.example.model;

public class Client {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Address address;
    
    public Client() {}
    
    public Client(String id, String name, String email, String phone, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    @Override
    public String toString() {
        return "Client{id='" + id + "', name='" + name + "', email='" + email + 
               "', phone='" + phone + "', address=" + address + "}";
    }
}
