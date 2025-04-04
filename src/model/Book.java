package model;

import java.util.Objects;

public class Book {

    private final int id; //Уникальный id книги в библиотеке
    private String title; // Название книги
    private String author; // Автор
    private String dateYear; // Дата год издания
    private String bookGenre;
    private User readingUser; //

    public Book(int id, String title, String author, String dateYear, String bookGenre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.dateYear = dateYear;
        this.bookGenre = bookGenre;
        this.readingUser = null;
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

    public String getDateYear() {
        return dateYear;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public User getReadingUser() {
        return readingUser;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDateYear(String dateYear) {
        this.dateYear = dateYear;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public void setReadingUser(User readingUser) {
        this.readingUser = readingUser;
    }

    @Override
    public String toString() {
        return String.format("Книга { id=%s, название: %s, автор: %s, жанр: %s, год издания: %s, на руках: %s}", id,title,author,bookGenre,dateYear,readingUser);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id == book.id && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(dateYear, book.dateYear) && Objects.equals(bookGenre, book.bookGenre) && Objects.equals(readingUser, book.readingUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, dateYear, bookGenre, readingUser);
    }


}
