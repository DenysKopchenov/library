package com.dkop.library.entity;

import java.util.Objects;

public class User {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, role, status);
    }

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String status;

    private User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Builder newBuilder() {
        return new User().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder firstName(String firstName) {
            User.this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            User.this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            User.this.email = email;
            return this;
        }

        public Builder password(String password) {
            User.this.password = password;
            return this;
        }

        public Builder role(String role) {
            User.this.role = role;
            return this;
        }

        public Builder status(String status) {
            User.this.status = status;
            return this;
        }

        public Builder id(int id) {
            User.this.id = id;
            return this;
        }

        public User build() {
            return User.this;
        }
    }
}
