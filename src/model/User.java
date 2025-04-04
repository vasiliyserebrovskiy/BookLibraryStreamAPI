package model;

import java.util.Objects;


public class User {

    private String email; // Для авторизации
    private String password; // Для авторизации
    private final int userId; // Для поиска пользователя по id
    private Role role;

    public User(int userId, String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("Пользователь { id = %d, email = \"%s\", роль = \"%s\" } ", userId, email, role);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(email, user.email) && Objects.equals(password, user.password) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, userId, role);
    }
}
