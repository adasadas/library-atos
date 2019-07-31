package com.adasadas.Library;

import com.adasadas.Book;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Library {
    final AtomicLong NEXT_ID = new AtomicLong(0);
    final Map<Long, LendableBook>  idToBookMap = new HashMap<>();

    public void addBook(Book book) {
        if(nonNull(book)) {
            idToBookMap.put(NEXT_ID.getAndIncrement(), new LendableBook(book));
        }
    }

    public void removeBook(long id) {
        if (idToBookMap.containsKey(id) && nonNull(idToBookMap.get(id)) && ! idToBookMap.get(id).isLent()) {
            idToBookMap.remove(id);
            System.out.println("book " + id + " removed");
        } else {
            System.out.println("book " + id + " can't be removed");
        }
    }

    public void listBooks() {
        Map<LendableBook, MutablePair<Integer, Integer>> bookToPairOfLentAndAvailableMap = new HashMap<>();

        for (Map.Entry<Long, LendableBook> entry : idToBookMap.entrySet()) {
            LendableBook book = entry.getValue();
            MutablePair<Integer, Integer> lentAndAvailableCount = bookToPairOfLentAndAvailableMap.get(book);
            if (nonNull(lentAndAvailableCount)) {
                if (book.isLent()) {
                    lentAndAvailableCount.left++;
                } else {
                    lentAndAvailableCount.right++;
                }
            } else {
                if (book.isLent()) {
                    bookToPairOfLentAndAvailableMap.put(book,
                            new MutablePair<Integer, Integer>(1, 0));
                } else {
                    bookToPairOfLentAndAvailableMap.put(book,
                            new MutablePair<Integer, Integer>(0, 1));
                }
            }
        }

        for (Map.Entry<LendableBook, MutablePair<Integer, Integer>> entry : bookToPairOfLentAndAvailableMap.entrySet()) {
            System.out.println("title: " + entry.getKey().getBook().getTitle() + " available: " +
                    entry.getValue().right + " lent: " + entry.getValue().left);
        }
    }

    public void findBooks(String title, String author, Integer year) {
        idToBookMap.entrySet().stream()
                .filter(entry -> isNull(title) || entry.getValue().getBook().getTitle().matches("(?i).*" + title + ".*"))
                .filter(entry -> isNull(author) || entry.getValue().getBook().getAuthor().matches("(?i).*" + author + ".*"))
                .filter(entry -> isNull(year) || entry.getValue().getBook().getYear().equals(year))
                .forEach(entry -> System.out.println("id: " + entry.getKey() +
                        " title: " + entry.getValue().getBook().getTitle() +
                        " author: " + entry.getValue().getBook().getAuthor() +
                        " year: " + entry.getValue().getBook().getYear() +
                        " isLent: " + entry.getValue().isLent()));
    }

    public void lendBook(long id, String lendingPersonName) {
        LendableBook book = idToBookMap.get(id);
        if (! idToBookMap.containsKey(id)) {
            System.out.println("book does not exist!");
            return;
        }

        if (book.isLent()) {
            System.out.println("You can't lend this book now because it's already lent by: " + book.getLendingPersonName());
        } else {
            book.setLent(true);
            book.setLendingPersonName(lendingPersonName);
            System.out.println("book lent successfully!");
        }
    }

    public void showAllBooksDetails(long id) {
        LendableBook book = idToBookMap.get(id);
        if(nonNull(book)) {
            boolean isLent = book.isLent();
            String lentBy = isLent ? " lent by: " + book.getLendingPersonName() : "";

        System.out.println("id: " + id +
                " title: " + book.getBook().getTitle() +
                " author: " + book.getBook().getAuthor() +
                " year: " + book.getBook().getYear() +
                " isLent: " + isLent + lentBy);
        } else {
            System.out.println("book not found");
        }
    }
}
