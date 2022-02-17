package com.dkop.library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private int id;
    private int userId;
    private int bookId;
    private String type;
    private String status;
    private LocalDate approvedDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;


    private Order() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", bookId=").append(bookId);
        sb.append(", status='").append(status).append('\'');
        sb.append(", approvedDate=").append(approvedDate);
        sb.append(", expectedReturnDate=").append(expectedReturnDate);
        sb.append(", actualReturnDate=").append(actualReturnDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && userId == order.userId && bookId == order.bookId && status.equals(order.status) && Objects.equals(approvedDate, order.approvedDate) && Objects.equals(expectedReturnDate, order.expectedReturnDate) && Objects.equals(actualReturnDate, order.actualReturnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, bookId, status, approvedDate, expectedReturnDate, actualReturnDate);
    }

    public static Builder newBuilder() {
        return new Order().new Builder();
    }
    public class Builder {

        private Builder() {
        }

        public Builder id(int id) {
            Order.this.id = id;
            return this;
        }

        public Builder userId(int userId) {
            Order.this.userId = userId;
            return this;
        }

        public Builder bookId(int bookId) {
            Order.this.bookId = bookId;
            return this;
        }

        public Builder type(String type) {
            Order.this.type = type;
            return this;
        }

        public Builder status(String status) {
            Order.this.status = status;
            return this;
        }

        public Builder approvedDate(LocalDate approvedDate) {
            Order.this.approvedDate = approvedDate;
            return this;
        }

        public Builder expectedReturnDate(LocalDate expectedReturnDate) {
            Order.this.expectedReturnDate = expectedReturnDate;
            return this;
        }

        public Builder actualReturnDate(LocalDate actualReturnDate) {
            Order.this.actualReturnDate = actualReturnDate;
            return this;
        }

        public Order build() {
            return Order.this;
        }
    }
}
