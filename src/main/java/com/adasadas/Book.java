package com.adasadas;

public class Book {

    private String title;
    private String author;
    private Integer year;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getYear() {
        return year;
    }

    public Book(String title, String author, Integer year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!title.equals(book.title)) return false;
        if (author != null ? !author.equals(book.author) : book.author != null) return false;
        return year != null ? year.equals(book.year) : book.year == null;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "title: " + title +
                " author: " + author +
                " year: " + year;
    }
}
