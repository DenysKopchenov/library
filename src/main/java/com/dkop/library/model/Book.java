package com.dkop.library.model;


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

    public int getOnOrder() {
        return onOrder;
    }

    public void setOnOrder(int onOrder) {
        this.onOrder = onOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Book(int id, String title, String author, String publisher, LocalDate publishingDate, int amount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishingDate = publishingDate;
        this.amount = amount;
    }

    public Book(String title, String author, String publisher, LocalDate publishingDate, int amount) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishingDate = publishingDate;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return title.equals(book.title) && author.equals(book.author) && publisher.equals(book.publisher) && publishingDate.equals(book.publishingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publisher, publishingDate);
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
}
