package com.dkop.library.dto;

import com.dkop.library.entity.Book;
import com.dkop.library.entity.User;

import java.time.LocalDate;

public class UserOrderDto {
    private LocalDate createDate;
    private LocalDate expectedReturnDate;
    private Book book;
    private User user;
    private String penalty;
    private int orderId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        final StringBuilder sb = new StringBuilder("UserOrderDto{");
        sb.append("createDate=").append(createDate);
        sb.append(", expectedReturnDate=").append(expectedReturnDate);
        sb.append(", book=").append(book);
        sb.append(", user=").append(user);
        sb.append(", penalty='").append(penalty).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append('}');
        return sb.toString();
    }
}
