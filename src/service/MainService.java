package service;

import model.Book;
import model.User;
import utils.MyList;

import java.util.List;

public interface MainService {
    // Методу по работе с пользователем

    // Методу по работе с пользователем

    User createUser(String email, String password); //++

    User getUserByEmail(String email); //++

    User getUserById(int id);

    List<User> getAllUsers(); // Для отображения всех зарегистрированных пользователей

    User updatePassword(String newPassword); //++

    User deleteUser(String email);

    User giveUserAdminRole(int id);

    boolean login(String email, String password);

    boolean logout(); //++

    User getActiveUser(); //++

    User unblockUser(String email);

    User unblockUser(int userId);

    User blockUser(String email);

    User blockUser(int userId);

    // Методы по работе с книгами
    List<Book> getAllBooks(); //++

    List<Book> getAvailableBooks(); //++

    List<Book> getBorrowedBooks(); //++

    List<Book> getMyBooks(); //++

    Book createBook(String title, String author, String dateYear, String bookGenre); //++

    Book getBookById(int id); //++

    List<Book> getBooksByTitle(String title); // contains

    List<Book> getBooksByAuthor(String author); // contains

    // Взять книгу из библиотеки
    Book userGetBook(int bookId);

    // Вернуть книгу
    Book userReturnBook(int bookId);

    User whoReadBook(int id);

    Book deleteBookById(int id);

    // Изменение данных о книге
    Book updateTitle(int id, String title);

    Book updateAuthor(int id, String author);

    Book updateDateYear(int id, String dateYear);

    Book updateGenre(int id, String bookGenre);


}
