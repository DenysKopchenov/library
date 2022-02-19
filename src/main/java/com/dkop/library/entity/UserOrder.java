package com.dkop.library.entity;

import java.time.LocalDate;

public class UserOrder {
    private LocalDate createDate;
    private LocalDate expectedReturnDate;
    private Book book;
    private String penalty;
    private int orderId;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    //todo not like this!

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserOrder{");
        sb.append("createDate=").append(createDate);
        sb.append(", expectedReturnDate=").append(expectedReturnDate);
        sb.append(", book=").append(book);
        sb.append(", penalty='").append(penalty).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }

    private String penaltyFormatter(String penalty) {
        StringBuilder builder = new StringBuilder();
        if (penalty.length() < 3) {
            builder.append("0.").append(penalty);
            return penalty;
        } else {
            builder.append(penalty, 0, penalty.length() - 2)
                    .append(".")
                    .append(penalty, penalty.length() - 2, penalty.length());
        }
        return builder.toString();
    }
}
