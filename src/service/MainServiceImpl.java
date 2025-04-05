package service;

import model.Book;
import model.Role;
import model.User;
import repository.BookRepository;
import repository.UserRepository;
import utils.EmailValidateException;
import utils.PasswordValidateException;
import utils.UserValidation;

import java.util.List;
import java.util.Optional;


public class MainServiceImpl implements MainService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private User activeUser;

    public MainServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.activeUser = null;
    }

    // Users
    @Override
    public User createUser(String email, String password) throws EmailValidateException, PasswordValidateException {

        UserValidation.validateEmail(email);
        UserValidation.validatePassword(password);

        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            return userRepository.addUser(email, password);
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(int id) {
        Optional<User> user = userRepository.getUserById(id);
        return user.orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User updatePassword(String newPassword) throws PasswordValidateException {
        UserValidation.validatePassword(newPassword);
        return userRepository.updatePassword(activeUser.getEmail(), newPassword);
    }

    @Override
    public User deleteUser(String email) {
        if (activeUser.getRole() == Role.ADMIN) {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                return userRepository.deleteUser(email);
            }
        }
        return null;
    }

    @Override
    public User blockUser(String email) {

        if (activeUser.getRole() == Role.ADMIN) {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                return userRepository.blockUser(email);
            }
        }
        return null;
    }

    @Override
    public User blockUser(int userId) {
        if (userId > 0 && activeUser.getRole() == Role.ADMIN) {
            Optional<User> user = userRepository.getUserById(userId);
            if (user.isPresent()) {
                return userRepository.blockUser(userId).get(); // уже проверили существование usrId
            }
        }
        return null;
    }

    @Override
    public User unblockUser(String email) {
        if (activeUser.getRole() == Role.ADMIN) {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                return userRepository.unblockUser(email);
            }
        }
        return null;
    }

    @Override
    public User unblockUser(int userId) {
        if (userId > 0 && activeUser.getRole() == Role.ADMIN) {
            Optional<User> user = userRepository.getUserById(userId);
            if (user.isPresent()) {
                return userRepository.unblockUser(userId).get();
            }
        }
        return null;
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    @Override
    public User giveUserAdminRole(int userId) {
        if (userId > 0) {
            if (activeUser != null && activeUser.getRole() == Role.ADMIN) {
                Optional<User> user = userRepository.getUserById(userId);
                if (user.isPresent()) {
                    return userRepository.giveUserAdminRole(userId).get();
                }
            }
        }
        return null;
    }

    @Override
    public boolean login(String email, String password) {
        if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    activeUser = user;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean logout() {
        if (activeUser == null) {
            return false; // нет активного пользователя
        }
        activeUser = null;
        return true; //  выход
    }


    // Books

    @Override
    public Book createBook(String title, String author, String dateYear, String bookGenre) {

        if (!title.isEmpty() && !author.isEmpty() && !dateYear.isEmpty() && !bookGenre.isEmpty()
                && !isSpaces(title) && !isSpaces(author) && !isSpaces(dateYear) && !isSpaces(bookGenre) && isInteger(dateYear)) {

            return bookRepository.addBook(title, author, dateYear, bookGenre);
        }

        return null;
    }

    @Override
    public Book getBookById(int id) {
        if (id > 0) {
            return bookRepository.getBookById(id);
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

    @Override
    public List<Book> getBorrowedBooks() {
        return bookRepository.getBorrowedBooks();
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        if (!title.isEmpty()) {
            return bookRepository.getBooksByTitle(title);
        }
        return null;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        if (!author.isEmpty()) {
            return bookRepository.getBooksByAuthor(author);
        }
        return null;
    }

    @Override
    public Book userGetBook(int bookId) {
        Book book;
        if (bookId > 0) {
            book = bookRepository.getBookById(bookId);
            if (book != null && book.getReadingUser() == null && activeUser.getRole() != Role.BLOCKED) {
                book = bookRepository.userGetBook(bookId, activeUser);
                return book;
            }
            return null;
        }

        return null;
    }

    @Override
    public Book userReturnBook(int bookId) {
        Book book;
        if (bookId > 0) {
            book = bookRepository.getBookById(bookId);
            if (book != null && book.getReadingUser() != null && book.getReadingUser().equals(activeUser)) {
                book = bookRepository.userReturnBook(bookId);
                return book;
            }
            return null;
        }
        return null;
    }

    @Override
    public Book deleteBookById(int id) {
        if (id > 0) {
            if (bookRepository.isBookExist(id)) {
                Book book = bookRepository.getBookById(id);
                if (book.getReadingUser() == null) {
                    return bookRepository.deleteBookById(id);
                }
            }
        }
        return null;
    }

    @Override
    public List<Book> getMyBooks() {
        return bookRepository.getMyBooks(activeUser);
    }


    @Override
    public Book updateTitle(int id, String title) {
        if (id > 0 && !title.isEmpty() && !isSpaces(title)) {
            if (bookRepository.isBookExist(id)) {
                //проверяем что книга не на руках
                User user = bookRepository.getReadingUser(id);
                if (user == null) {
                    return bookRepository.updateTitle(id, title);
                }
            }
        }
        return null;
    }

    @Override
    public Book updateAuthor(int id, String author) {
        if (id > 0 && !author.isEmpty() && !isSpaces(author)) {
            if (bookRepository.isBookExist(id)) {
                //проверяем что книга не на руках
                User user = bookRepository.getReadingUser(id);
                if (user == null) {
                    return bookRepository.updateAuthor(id, author);
                }
            }
        }
        return null;
    }

    @Override
    public Book updateDateYear(int id, String dateYear) {
        if (id > 0 && !dateYear.isEmpty() && !isSpaces(dateYear) && isInteger(dateYear)) {
            if (bookRepository.isBookExist(id)) {
                //проверяем что книга не на руках
                User user = bookRepository.getReadingUser(id);
                if (user == null) {
                    return bookRepository.updateDateYear(id, dateYear);
                }
            }
        }
        return null;
    }

    @Override
    public Book updateGenre(int id, String bookGenre) {
        if (id > 0 && !bookGenre.isEmpty() && !isSpaces(bookGenre)) {
            if (bookRepository.isBookExist(id)) {
                User user = bookRepository.getReadingUser(id);
                if (user == null) {
                    return bookRepository.updateGenre(id, bookGenre);
                }
            }
        }
        return null;
    }

    @Override
    public User whoReadBook(int id) {
        if (id > 0) {
            if (bookRepository.isBookExist(id)) {
                return bookRepository.getReadingUser(id);
            }
        }
        return null;
    }

    private boolean isSpaces(String str) {
        return str.matches("\\s+"); // пробелы 1 и более
    }

    private boolean isInteger(String str) {
        return str.matches("\\d+"); // Только положительные целые числа
    }
}
