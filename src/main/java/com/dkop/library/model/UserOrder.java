package com.dkop.library.model;

import java.time.LocalDate;

public class UserOrder {
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private Book book;
    private String penalty;

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

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserOrder{");
        sb.append("expectedReturnDate=").append(expectedReturnDate);
        sb.append(", actualReturnDate=").append(actualReturnDate);
        sb.append(", book=").append(book);
        sb.append(", penalty=").append(penaltyFormatter(penalty));
        sb.append('}');
        return sb.toString();
    }

    //todo not like this!
    private String penaltyFormatter(String penalty) {
        StringBuilder builder = new StringBuilder();
        if (penalty.length() <= 3) {
            return penalty;
        } else {
            builder.append(penalty, 0, penalty.length() - 2)
                    .append(".")
                    .append(penalty, penalty.length() - 2, penalty.length());
        }
        return builder.toString();
    }
}
