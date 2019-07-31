package com.adasadas.Library;

import com.adasadas.Book;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class LibraryTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private Library library;
    private final Book book1 = new Book("t1", "a1", 1111);
    private final Book book2 = new Book("t2", "a2", 2222);
    private final Book book3 = new Book("t3", "a3", 3333);

    private final Book book_PanTadeusz = new Book("Pan Tadeusz", "Adam Mickiewicz", 1834);
    private final Book book_PanKleks = new Book("Oto Pan Kleks", "Pan Jan", 1800);
    private final Book book_Dziady = new Book("Dziady", "Adam Mickiewicz", 1800);
    private final Book book_PanTadeusz2 = new Book("Pan Tadeusz2", "Pan Jan", 1801);
    private final Book book_Trees = new Book("Trees", "abc", 1801);

    @Before
    public void beforeTest() {
        library = new Library();
    }

    @Test
    public void shouldAddBooks() {
        library.addBook(book1);
        library.addBook(book2);

        assertEquals(2, library.idToBookMap.size());
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book1)));
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book2)));
    }

    @Test
    public void shouldAddBooksWithUniqueId() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book1);

        assertEquals(4, library.idToBookMap.size());
        assertEquals(4, library.idToBookMap.keySet().size());
    }

    @Test
    public void shouldNotAddNullBook() {
        library.addBook(null);

        assertEquals(0, library.idToBookMap.size());
    }

    @Test
    public void shouldRemoveBookById() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        assertEquals(3, library.idToBookMap.size());

        library.removeBook(1);

        assertEquals(2, library.idToBookMap.size());
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book1)));
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book3)));
    }

    @Test
    public void shouldNotRemoveBookByIdWhenItIsLent() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        assertEquals(3, library.idToBookMap.size());
        library.lendBook(1, "John");
        library.removeBook(1);

        assertEquals(3, library.idToBookMap.size());
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book1)));
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book2)));
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book3)));
    }

    @Test
    public void shouldNotRemoveBookByIdWhenWrongId() {
        library.addBook(book1);

        assertEquals(1, library.idToBookMap.size());
        library.removeBook(1);

        assertEquals(1, library.idToBookMap.size());
        assertTrue(library.idToBookMap.containsValue(new LendableBook(book1)));
    }

    @Test
    public void shouldListAllBooksDistinct() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book3);
        library.lendBook(4, "John");
        library.lendBook(5, "John");

        assertEquals(6, library.idToBookMap.size());

        System.setOut(new PrintStream(outContent));
        library.listBooks();
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("title: t1 available: 1 lent: 0"));
        assertThat(print, CoreMatchers.containsString("title: t2 available: 2 lent: 0"));
        assertThat(print, CoreMatchers.containsString("title: t3 available: 1 lent: 2"));
        assertEquals(96, print.length());
    }

    @Test
    public void shouldFindBooksByTitle() {
        library.addBook(book_Dziady);
        library.addBook(book_PanKleks);
        library.addBook(book_PanTadeusz);

        System.setOut(new PrintStream(outContent));
        library.findBooks("pan", null, null);
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("id: 1 title: Oto Pan Kleks author: Pan Jan year: 1800 isLent: false"));
        assertThat(print, CoreMatchers.containsString("id: 2 title: Pan Tadeusz author: Adam Mickiewicz year: 1834 isLent: false"));
        assertEquals(144, print.length());
    }

    @Test
    public void shouldFindBooksByAuthor() {
        library.addBook(book_Dziady);
        library.addBook(book_PanKleks);
        library.addBook(book_PanTadeusz);

        System.setOut(new PrintStream(outContent));
        library.findBooks(null, "micKi", null);
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("id: 0 title: Dziady author: Adam Mickiewicz year: 1800 isLent: false"));
        assertThat(print, CoreMatchers.containsString("id: 2 title: Pan Tadeusz author: Adam Mickiewicz year: 1834 isLent: false"));
        assertEquals(145, print.length());
    }

    @Test
    public void shouldFindBooksByYear() {
        library.addBook(book_Dziady);
        library.addBook(book_PanKleks);
        library.addBook(book_PanTadeusz);

        System.setOut(new PrintStream(outContent));
        library.findBooks(null, null, 1800);
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("id: 0 title: Dziady author: Adam Mickiewicz year: 1800 isLent: false"));
        assertThat(print, CoreMatchers.containsString("id: 1 title: Oto Pan Kleks author: Pan Jan year: 1800 isLent: false"));
        assertEquals(139, print.length());
    }

    @Test
    public void shouldFindBooksByTitleAuthorAndYear() {
        library.addBook(book_Dziady);
        library.addBook(book_PanKleks);
        library.addBook(book_PanTadeusz);
        library.addBook(book_PanTadeusz2);
        library.addBook(book_Trees);

        System.setOut(new PrintStream(outContent));
        library.findBooks("pan", "jan", 1801);
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("id: 3 title: Pan Tadeusz2 author: Pan Jan year: 1801 isLent: false"));
        assertEquals(68, print.length());
    }

    @Test
    public void shouldFindBooksAndShowWhetherLent() {
        library.addBook(book_Dziady);
        library.addBook(book_PanKleks);
        library.addBook(book_PanTadeusz);
        library.lendBook(1, "Jane");

        System.setOut(new PrintStream(outContent));
        library.findBooks("pan", null, null);
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("id: 1 title: Oto Pan Kleks author: Pan Jan year: 1800 isLent: true lent by: Jane"));
        assertThat(print, CoreMatchers.containsString("id: 2 title: Pan Tadeusz author: Adam Mickiewicz year: 1834 isLent: false"));
        assertEquals(157, print.length());
    }

    @Test
    public void shouldAllowToLentBookById() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        library.lendBook(1, "Carl");

        assertEquals(3, library.idToBookMap.size());

        assertFalse(library.idToBookMap.get(0L).isLent());
        assertTrue(library.idToBookMap.get(1L).isLent());
        assertFalse(library.idToBookMap.get(2L).isLent());
    }

    @Test
    public void shouldNotAllowToLentBookByIdWhenAlreadyLentAndShouldShowWhoLent() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        library.lendBook(1, "Carl");

        assertFalse(library.idToBookMap.get(0L).isLent());
        assertTrue(library.idToBookMap.get(1L).isLent());
        assertFalse(library.idToBookMap.get(2L).isLent());

        System.setOut(new PrintStream(outContent));
        library.lendBook(1, "James");
        String print = outContent.toString();

        assertThat(print, CoreMatchers.containsString("You can't lend this book now because it's already lent by: Carl"));
        assertFalse(library.idToBookMap.get(0L).isLent());
        assertTrue(library.idToBookMap.get(1L).isLent());
        assertFalse(library.idToBookMap.get(2L).isLent());
    }

    @Test
    public void shouldNotAllowToLentNotExistingBook() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.lendBook(100, "Carl");

        assertFalse(library.idToBookMap.get(0L).isLent());
        assertFalse(library.idToBookMap.get(1L).isLent());
        assertFalse(library.idToBookMap.get(2L).isLent());
    }

    @Test
    public void shouldPrintAllBooksDetailsById() {
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        library.lendBook(1, "Carl");

        System.setOut(new PrintStream(outContent));
        library.showAllBooksDetails(0);
        library.showAllBooksDetails(1);
        String print = outContent.toString();

        assertEquals(print,"id: 0 title: t1 author: a1 year: 1111 isLent: false\r\n" +
                "id: 1 title: t2 author: a2 year: 2222 isLent: true lent by: Carl\r\n");
    }
}
