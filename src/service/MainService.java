package service;

import model.Book;
import model.User;
import utils.MyList;

public interface MainService {
    // Методу по работе с пользователем

    // Методу по работе с пользователем

    User createUser(String email, String password); //++

    User getUserByEmail(String email); //++

    User getUserById(int id);

    MyList<User> getAllUsers(); // Для отображения всех зарегистрированных пользователей

    User updatePassword(String newPassword); //++

    boolean deleteUser(String email);

    User giveUserAdminRole(int id);

    boolean login(String email, String password);

    boolean logout(); //++

    User getActiveUser(); //++

    User unblockUser(String email);

    User unblockUser(int userId);

    User blockUser(String email);

    User blockUser(int userId);

    // Методы по работе с книгами
    MyList<Book> getAllBooks(); //++

    MyList<Book> getAvailableBooks(); //++

    MyList<Book> getBorrowedBooks(); //++

    MyList<Book> getMyBooks(); //++

    Book createBook(String title, String author, String dateYear, String bookGenre); //++

    Book getBookById(int id); //++

    MyList<Book> getBooksByTitle(String title); // contains

    MyList<Book> getBooksByAuthor(String author); // contains

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
