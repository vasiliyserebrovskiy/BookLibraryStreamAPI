package repository;

import model.Book;
import model.User;
import utils.MyList;

public interface BookRepository {

    Book addBook(String title, String author, String dateYear, String bookGenre);

    Book getBookById(int id);

    MyList<Book> getAllBooks();

    MyList<Book> getAvailableBooks();

    MyList<Book> getBorrowedBooks();

    MyList<Book> getMyBooks(User user);

    MyList<Book> getBooksByTitle(String title); // contains

    MyList<Book> getBooksByAuthor(String author); // contains

    Book userGetBook(int id, User user);

    Book userReturnBook(int id);

    boolean isBookExist(int id);

    Book deleteBookById(int id);

    Book updateTitle(int id, String title);

    Book updateAuthor(int id, String author);

    Book updateDateYear(int id, String dateYear);

    Book updateGenre(int id, String bookGenre);

    User getReadingUser(int id);

}
