package app.entities;

import java.util.Objects;

public class User {
    private int userId;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String address;

    public User(int userId, String email, String password, String role, String phone, String address) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    public User(String email, String password, String role, String phone, String address) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    public User(int userId, String email, String role, String phone, String address) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(phone, user.phone) && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, password, role, phone, address);
    }
}
