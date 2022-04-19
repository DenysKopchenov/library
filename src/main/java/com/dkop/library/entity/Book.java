package com.dkop.library.entity;


import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishingDate;
    private int amount;
    private int onOrder;

    private Book() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getOnOrder() {
        return onOrder;
    }

    public void setOnOrder(int onOrder) {
        this.onOrder = onOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && amount == book.amount && onOrder == book.onOrder && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(publisher, book.publisher) && Objects.equals(publishingDate, book.publishingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, publisher, publishingDate, amount, onOrder);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append(", publishingDate=").append(publishingDate);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }

    public static Builder newBuilder() {
        return new Book().new Builder();
    }

    public class Builder {
        private Builder() {

        }

        public Builder id(int id) {
            Book.this.id = id;
            return this;
        }

        public Builder title(String title) {
            Book.this.title = title;
            return this;
        }

        public Builder author(String author) {
            Book.this.author = author;
            return this;
        }

        public Builder publisher(String publisher) {
            Book.this.publisher = publisher;
            return this;
        }

        public Builder publishingDate(LocalDate publishingDate) {
            Book.this.publishingDate = publishingDate;
            return this;
        }

        public Builder amount(int amount) {
            Book.this.amount = amount;
            return this;
        }

        public Builder onOrder(int onOrder) {
            Book.this.onOrder = onOrder;
            return this;
        }

        public Book build() {
            return Book.this;
        }
    }
}
