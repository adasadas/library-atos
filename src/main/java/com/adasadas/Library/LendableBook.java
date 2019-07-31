package com.adasadas.Library;

import com.adasadas.Book;

class LendableBook implements Lendable {
    private boolean isLent = false;
    private String lendingPersonName;
    private Book book;

    LendableBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    @Override
    public boolean isLent() {
        return isLent;
    }

    @Override
    public void setLent(boolean isLent) {
        this.isLent = isLent;
    }

    @Override
    public String getLendingPersonName() {
        return lendingPersonName;
    }

    @Override
    public void setLendingPersonName(String lendingPersonName) {
        this.lendingPersonName = lendingPersonName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LendableBook book1 = (LendableBook) o;

        return book.equals(book1.book);
    }

    @Override
    public int hashCode() {
        return book.hashCode();
    }

    @Override
    public String toString() {
        String lentBy = isLent ? " lent by: " + lendingPersonName : "";
        return book + " isLent: " + isLent + lentBy;
    }
}
