
package service;

import model.Book;
import model.Role;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import repository.BookRepository;
import repository.BookRepositoryImpl;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import utils.EmailValidateException;
import utils.PasswordValidateException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MainServiceTests {

    BookRepository bookRepository;
    UserRepository userRepository;
    MainService service;

    @BeforeEach
    void setUp() {
        // Метод создающий изначальную архитектуру системы перед каждым тестом
        bookRepository = new BookRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        service = new MainServiceImpl(bookRepository, userRepository);
    }

    // Тесты для методов по пользователю -------------------
    // Проверка метода createUser --------------------------------------------------------------
    @ParameterizedTest
    @MethodSource("testCreateUser")
    void testValidCreateUser(String email, String password) {

        assertDoesNotThrow(() -> {
            User user = service.createUser(email, password);
            assertNotEquals(null, user); //Объект должен был создаться
            assertEquals(email, user.getEmail()); // проверка email
            assertEquals(password, user.getPassword()); //проверка пароля
        });
    }

    static Stream<Arguments> testCreateUser() {
        return Stream.of(
                Arguments.of("test@test.com", "P@ssw0rd"),
                Arguments.of("test2@test.ru", "P@ssw0rd1"),
                Arguments.of("test34@mail.ru", "P@ssw0rd12")
        );
    }

    // Попытка заведения пользователя с некорректными email
    @ParameterizedTest
    @MethodSource("testNotEmailCreateUser")
    void testNotValidEmailCreateUser(String email, String password) {

        assertThrows(EmailValidateException.class, () -> {
            User user = service.createUser(email, password);
            assertNull(user); //Пользователь не должен был создаться
        });
    }

    static Stream<Arguments> testNotEmailCreateUser() {
        return Stream.of(
                Arguments.of("test@@test.com", "P@ssw0rd"),
                Arguments.of("1test2@mail.ru", "1qazXsw@"),
                Arguments.of("test2@mailr.u", "1qazXsw@"),
                Arguments.of("test2@mail.ru.", "1qazXsw@")
        );
    }

    // Попытка заведения пользователя с некорректными password
    @ParameterizedTest
    @MethodSource("testNotPasswordCreateUser")
    void testNotValidPasswordCreateUser(String email, String password) {

        assertThrows(PasswordValidateException.class, () -> {
            User user = service.createUser(email, password);
            assertNull(user); //Пользователь не должен был создаться
        });
    }

    static Stream<Arguments> testNotPasswordCreateUser() {
        return Stream.of(
                Arguments.of("richy@richy.de", "qwerty"),
                Arguments.of("richy@richy.de", "QwertY"),
                Arguments.of("richy@richy.de", "Q!ertY"),
                Arguments.of("richy@richy.de", "Q1ertY"),
                Arguments.of("richy@richy.de", "Qw1@r")
        );
    }



    // Тестируем метод getUserByEmail
    @ParameterizedTest
    @MethodSource("testGetUserByEmail")
    void testValidGetUserByEmail(String email) {
        User user = service.getUserByEmail(email);
        assertNotNull(user);
    }

    static Stream<Arguments> testGetUserByEmail() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("user2@example.com"),
                org.junit.jupiter.params.provider.Arguments.of("user3@example.com"),
                org.junit.jupiter.params.provider.Arguments.of("user4@example.com")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidGetUserByEmail")
    void testNotValidGetUserByEmail(String email) {
        User user = service.getUserByEmail(email);
        assertNull(user);
    }

    static Stream<Arguments> testNotValidGetUserByEmail() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("user6@example.com"),
                org.junit.jupiter.params.provider.Arguments.of("user7@example.com"),
                org.junit.jupiter.params.provider.Arguments.of("user8@example.com")
        );
    }

    // Тестируем метод getUserById пока не используем - Виталий

    @ParameterizedTest
    @MethodSource("testValidGetUserById")
    void testValidGetUserById(int id){
        User user = service.getUserById(id);
        assertNotNull(user);
    }

    static Stream<Arguments> testValidGetUserById(){
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3)

        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidGetUserById")
    void testNotValidGetUserById(int id) {
        User user = service.getUserById(id);
        assertNull(user);
    }

    static Stream<Arguments> testNotValidGetUserById() {
        return Stream.of(
                Arguments.of(100),
                Arguments.of(200),
                Arguments.of(300)
        );
    }

    //Тестируем метод getAllUsers
    @Test
    void testGetAllUsers() {
        List<User> users = service.getAllUsers();
        assertNotNull(users);
    }

    //Проверка метода updatePassword
    @ParameterizedTest
    @MethodSource("testUpdatePassword")
    void testValidUpdatePassword(String email, String password, String newPassword) {

        // Пользователь должен быть залогиненым, для смены пароля
        service.login(email, password); // логинемся
        assertDoesNotThrow(() -> {
            User user = service.updatePassword(newPassword);
            assertEquals(newPassword, user.getPassword()); //проверка пароля
        });
    }

    static Stream<Arguments> testUpdatePassword() {
        return Stream.of(
                Arguments.of("user2@example.com", "Secure*987", "P@ssw0rd1"),
                Arguments.of("user3@example.com", "TestUser@22", "P@ssw0rd@"),
                Arguments.of("user4@example.com", "Pa$$w0rd99", "Qwert!y1")
        );
    }

    // Попытка заведения пользователя с некорректными данными
    @ParameterizedTest
    @MethodSource("testNotUpdatePassword")
    void testNotValidUpdatePassword(String email, String password, String newPassword) {

        // Пользователь должен быть залогиненым, для смены пароля
        service.login(email, password); // логинемся

        assertThrows(PasswordValidateException.class, () -> {
            User user = service.updatePassword(newPassword);
            assertNull(user); //проверка пароля

        });
    }

    static Stream<Arguments> testNotUpdatePassword() {
        return Stream.of(
                Arguments.of("user2@example.com", "Secure*987", "qwerty"),
                Arguments.of("user2@example.com", "Secure*987", "qweRty"),
                Arguments.of("user3@example.com", "TestUser@22", "Qwer3ty"),
                Arguments.of("user3@example.com", "TestUser@22", "QWE@R$TY"),
                Arguments.of("user4@example.com", "Pa$$w0rd99", "qwerty"),
                Arguments.of("user4@example.com", "Pa$$w0rd99", "Qw1@e")
        );
    }

    // Тестируем метод deleteUser - Тим
    @ParameterizedTest
    @ValueSource(strings = {"user4@example.com","user2@example.com"})
    void testValidDeleteUser(String email){
        service.login("1","1");

        User user = service.deleteUser(email);
        assertNotNull(user);
        user = service.getUserByEmail(email);
        assertNull(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"user33@example.com","user44@example.com"})
    void testNotValidDeleteUser(String email){
        service.login("1","1");
        User user = service.deleteUser(email);
        assertNull(user);

    }



    // Тестируем метод giveUserAdminRole - Игорь
    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4})
    void testGiveUserAdminRole(int userId) {
        service.login("1", "1");

        User user = service.giveUserAdminRole(userId);

        assertNotNull(user);
        assertEquals(Role.ADMIN, user.getRole());
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 99, -4})
    void testNotGiveUserAdminRole(int userId) {
        service.login("1", "1");

        User user = service.giveUserAdminRole(userId);

        assertNull(user);
    }



    //Проверка метода login() -------------------------------------------------------
    @ParameterizedTest
    @MethodSource("testLogin")
    void testValidLogin(String email, String password) {

        // Пользователь должен быть залогиненым, для смены пароля
        boolean isLogin = service.login(email, password); // логинемся
        assertTrue(isLogin);
        assertNotNull(service.getActiveUser());
    }

    static Stream<Arguments> testLogin() {
        return Stream.of(
                Arguments.of("user2@example.com", "Secure*987"),
                Arguments.of("user3@example.com", "TestUser@22"),
                Arguments.of("user4@example.com", "Pa$$w0rd99")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotLogin")
    void testNotValidLogin(String email, String password) {

        // Пользователь должен быть залогиненым, для смены пароля
        boolean isLogin = service.login(email, password); // логинемся
        assertFalse(isLogin);
        assertNull(service.getActiveUser());
    }

    static Stream<Arguments> testNotLogin() {
        return Stream.of(
                Arguments.of("user2@example.com", "P@ssw0rd"),
                Arguments.of("user3@example.com", "Pqssw0rd"),
                Arguments.of("user4@example.com", "P@ssw0rd345")
        );
    }

    // Проверка метода logout ---------------------------------------------------------------------------
    // В нашем случае некорректный logout не проверить. Он слишком простой.
    @ParameterizedTest
    @MethodSource("testLogout")
    void testValidLogout(String email, String password) {

        // Пользователь должен быть залогиненым, для смены пароля
        service.login(email, password); // логинемся
        service.logout();
        assertNull(service.getActiveUser());
    }

    static Stream<Arguments> testLogout() {
        return Stream.of(
                Arguments.of("user2@example.com", "Secure*987"),
                Arguments.of("user3@example.com", "TestUser@22"),
                Arguments.of("user4@example.com", "Pa$$w0rd99")
        );
    }

    // Проверка метода getActiveUser
    @ParameterizedTest
    @MethodSource("testValidGetActiveUser")
    void testValidGetActiveUser(String email, String password) {

        service.login(email, password);
        User expectedUser = service.getUserByEmail(email);
        User actualUser = service.getActiveUser();
        assertEquals(expectedUser, actualUser);
        assertEquals(expectedUser.getUserId(), actualUser.getUserId());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getRole(), actualUser.getRole());

    }

    static Stream<Arguments> testValidGetActiveUser() {
        return Stream.of(
                Arguments.of("user2@example.com", "Secure*987"),
                Arguments.of("user3@example.com", "TestUser@22"),
                Arguments.of("user4@example.com", "Pa$$w0rd99")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidGetActiveUser")
    void testNotValidGetActiveUser(String email, String password) {

        service.login(email, password);
        // User expectedUser = service.getUserByEmail(email);
        User actualUser = service.getActiveUser();
        assertNull(actualUser);

    }

    static Stream<Arguments> testNotValidGetActiveUser() {
        return Stream.of(
                Arguments.of("user5@example.com", "Secure*987"),
                Arguments.of("user6@example.com", "TestUser@22"),
                Arguments.of("user7@example.com", "Pa$$w0rd99")
        );
    }

    // Тестируем метод unblockUser by email
    @ParameterizedTest
    @MethodSource("testValidUnblockUser")
    void testValidUnblockUser(String email) {
        service.login("1", "1");
        User user = service.blockUser(email);
        assertNotNull(user);
        assertEquals(Role.BLOCKED, user.getRole());

    }

    static Stream<Arguments> testValidUnblockUser() {
        return Stream.of(
                Arguments.of("user2@example.com"),
                Arguments.of("user3@example.com"),
                Arguments.of("user4@example.com")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidUnblockUser")
    void testNotValidUnblockUser(String email) {
        service.login("1", "1");
        User user = service.blockUser(email);
        assertNull(user);
    }

    static Stream<Arguments> testNotValidUnblockUser() {
        return Stream.of(
                Arguments.of("user22@example.com"),
                Arguments.of("user33@example.com."),
                Arguments.of("user4@@example.com")
        );
    }

    // Тестируем метод unblockUser by id
    @ParameterizedTest
    @MethodSource("testValidUnblockUserById")
    void testValidUnblockUserById(int userId) {
        service.login("1" , "1");

        service.blockUser(userId);

        User user = service.unblockUser(userId);
        assertNotNull(user);
        assertEquals(Role.USER, user.getRole());
    }
    static Stream<Arguments> testValidUnblockUserById() {
        return Stream.of(
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4)
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidUnblockUserById")
    void testNotValidUnblockUserById(int userId) {
        service.login("1" , "1");

        User user = service.unblockUser(userId);
        assertNull(user);
    }
    static Stream<Arguments> testNotValidUnblockUserById() {
        return Stream.of(
                Arguments.of(99),
                Arguments.of(-1),
                Arguments.of(0)
        );
    }


    // Тестируем метод blockUser by email
    @ParameterizedTest
    @MethodSource("testValidBlockUserByEmail")
    void testValidBlockUserByEmail(String email) {
        service.login("1", "1");

        User user = service.blockUser(email);

        assertNotNull(user);
        assertEquals(Role.BLOCKED, user.getRole());
    }

    static Stream<Arguments> testValidBlockUserByEmail() {
        return Stream.of(
                Arguments.of("user2@example.com"),
                Arguments.of("user3@example.com"),
                Arguments.of("user4@example.com")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidBlockUserByEmail")
    void testNotValidBlockUserByEmail(String email) {
        service.login("1", "1");

        User user = service.blockUser(email);

        assertNull(user);
    }

    static Stream<Arguments> testNotValidBlockUserByEmail() {
        return Stream.of(
                Arguments.of("nonexistent@example.com"),
                Arguments.of("invalid@@example.com"),
                Arguments.of("")
        );
    }

    // Тестируем метод blockUser by id
    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    void testValidBlockUserById(int userId) {
        service.login("1", "1");
        User user = service.blockUser(userId);
        assertNotNull(user);
        assertEquals(Role.BLOCKED, user.getRole());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,-7,20})
    void testNotValidBlockUserById(int userId) {
        service.login("1", "1");
        User user = service.blockUser(userId);
        assertNull(user);
    }




    // ----------------------------------------------------------------------------------------------
    // ТЕСТЫ МЕТОДОВ ПО КНИГАМ
    @Test
    void testGetAllBook() {
        List<Book> books = service.getAllBooks();
        assertNotNull(books);
    }

    @Test
    void testGetAvailableBooks() {
        boolean login = service.login("user2@example.com", "Secure*987");
        //System.out.println(login);
        Book book = service.userGetBook(1);
        Book book2 = service.userGetBook(7);
//        System.out.println(book);
//        System.out.println(book2);
        List<Book> books = service.getAvailableBooks();

        assertEquals(9, books.size());
    }

    @Test
    void testGetAvailableBooks2() {
        boolean login = service.login("user2@example.com", "Secure*987");
        //System.out.println(login);

        List<Book> books = service.getAllBooks();
        for (int i = 1; i <= books.size(); i++) {
            service.userGetBook(i);
        }
        List<Book> availabldeBook = service.getAvailableBooks();
        assertEquals(0, availabldeBook.size());
    }

    // Тестируем метод getBorrowedBooks
    @Test
    void testGetBorrowedBooks() {
        boolean login = service.login("user2@example.com", "Secure*987");
        List<Book> borrowedBook = service.getBorrowedBooks();
        assertEquals(0, borrowedBook.size());
    }

    @Test
    void testGetBorrowedBooks2() {
        boolean login = service.login("user2@example.com", "Secure*987");
        Book book = service.userGetBook(1);
        Book book2 = service.userGetBook(7);
        List<Book> borrowedBook = service.getBorrowedBooks();

        assertEquals(2, borrowedBook.size());
    }

    // Тестируем метод getMyBooks
    @Test
    void testGetMyBook() {
        boolean login = service.login("user2@example.com", "Secure*987");
        List<Book> borrowedBook = service.getMyBooks();

        assertEquals(0, borrowedBook.size());
    }

    @Test
    void testGetMyBook2() {
        boolean login = service.login("user2@example.com", "Secure*987");
        Book book = service.userGetBook(1);
        Book book2 = service.userGetBook(7);
        Book book3 = service.userGetBook(8);
        List<Book> borrowedBook = service.getMyBooks();

        assertEquals(3, borrowedBook.size());
    }

    // Тестирование метода createBook
    @ParameterizedTest
    @MethodSource("testValidCreateBook")
    void testValidCreateBook(String title, String author, String dateYear, String bookGenre) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.createBook(title, author, dateYear, bookGenre);
        assertNotNull(book);
    }

    static Stream<Arguments> testValidCreateBook() {
        return Stream.of(
                Arguments.of("Test1", "Test Author", "1950", "Horror"),
                Arguments.of("Test2", "Test Author", "2000", "Komedy"),
                Arguments.of("Test3", "Test Author", "2020", "Роман")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidCreateBook")
    void testNotValidCreateBook(String title, String author, String dateYear, String bookGenre) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.createBook(title, author, dateYear, bookGenre);
        assertNull(book);
    }

    static Stream<Arguments> testNotValidCreateBook() {
        return Stream.of(
                Arguments.of("", "Test Author", "1950", "Horror"),
                Arguments.of("Test2", "", "2000", "Komedy"),
                Arguments.of("Test3", "Test Author", "", "Роман"),
                Arguments.of("", "", "", ""),
                Arguments.of("Test3", "Test Author", "2020", "")
        );
    }

    // Тестируем метод getBookById
    @ParameterizedTest
    @ValueSource(ints = {1, 4, 9})
    void testValidGetBookById(int id) {
        Book book = service.getBookById(id);
        assertNotNull(book);
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 42, 96})
    void testNotValidGetBookById(int id) {
        Book book = service.getBookById(id);
        assertNull(book);
    }

    //Тестируем метод getBooksByTitle

    // Тестируем метод getBooksByAuthor

    // Тестируем метод userGetBook
    @ParameterizedTest
    @ValueSource(ints = {1,4,6})
    void testValidUserGetBook(int id){
        service.login("2","2");
        Book book = service.userGetBook(id);
        assertNotNull(book);
        assertNotNull(book.getReadingUser());
    }

    @ParameterizedTest
    @ValueSource(ints = {12,43,67})
    void testNotValidUserGetBook(int id){
        service.login("2","2");
        Book book = service.userGetBook(id);
        assertNull(book);

    }

    // Тестируем метод userReturnBook
    @ParameterizedTest
    @ValueSource(ints = {1,4,6})
    void testValidUserReturnBook(int id){
        service.login("2","2");
        service.userGetBook(id);

        Book book = service.userReturnBook(id);
        assertNotNull(book);
        assertNull(book.getReadingUser());
    }

    @ParameterizedTest
    @ValueSource(ints = {12,43,67})
    void testNotValidUserReturnBook(int id){
        service.login("2","2");
        Book book = service.userReturnBook(id);
        assertNull(book);
    }


    // Тестируем метод whoReadBook

    // Тестируем метод deleteBookById

    // Тестируем метод updateTitle
    @ParameterizedTest
    @MethodSource("testValidUpdateTitle")
    void testValidUpdateTitle(int id, String title) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateTitle(id, title);
        assertNotNull(book);
        assertEquals(title, book.getTitle());
    }

    static Stream<Arguments> testValidUpdateTitle() {
        return Stream.of(
                Arguments.of("1", "Test Title"),
                Arguments.of("1", "Test Title 2")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidUpdateTitle")
    void testNotValidUpdateTitle(int id, String title) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateTitle(id, title);
        assertNull(book);
    }

    static Stream<Arguments> testNotValidUpdateTitle() {
        return Stream.of(
                Arguments.of("1", ""),
                Arguments.of("7", "  "),
                Arguments.of("25", "       "),
                Arguments.of("56", "Test Title 2")
        );
    }

    // Тестируем метод updateAuthor
    @ParameterizedTest
    @MethodSource("testValidUpdateAuthor")
    void testValidUpdateAuthor(int id, String author) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateAuthor(id, author);
        assertNotNull(book);
        assertEquals(author, book.getAuthor());
    }

    static Stream<Arguments> testValidUpdateAuthor() {
        return Stream.of(
                Arguments.of("1", "Test Author"),
                Arguments.of("1", "Test Author 2")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidUpdateAuthor")
    void testNotValidUpdateAuthor(int id, String author) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateAuthor(id, author);
        assertNull(book);
    }

    static Stream<Arguments> testNotValidUpdateAuthor() {
        return Stream.of(
                Arguments.of("1", ""),
                Arguments.of("0", "New Author"),
                Arguments.of("7", "  "),
                Arguments.of("25", "       "),
                Arguments.of("56", "Test Title 2")
        );
    }


    // Тестируем метод updateDateYear
    @ParameterizedTest
    @MethodSource("testValidUpdateDateYear")
    void testValidUpdateDateYear(int id, String dateYear) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateDateYear(id, dateYear);
        assertNotNull(book);
        assertEquals(dateYear, book.getDateYear());
    }

    static Stream<Arguments> testValidUpdateDateYear() {
        return Stream.of(
                Arguments.of("1", "1900"),
                Arguments.of("1", "1923")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidUpdateDateYear")
    void testNotValidUpdateDateYear(int id, String dateYear) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateDateYear(id, dateYear);
        assertNull(book);
    }

    static Stream<Arguments> testNotValidUpdateDateYear() {
        return Stream.of(
                Arguments.of("1", ""),
                Arguments.of("0", "1982"),
                Arguments.of("7", "  "),
                Arguments.of("25", "       "),
                Arguments.of("56", "Test Title 2")
        );
    }

    // Тестируем метод updateGenre
    @ParameterizedTest
    @MethodSource("testValidUpdateGenre")
    void testValidUpdateGenre(int id, String bookGenre) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateGenre(id, bookGenre);
        assertNotNull(book);
        assertEquals(bookGenre, book.getBookGenre());
    }

    static Stream<Arguments> testValidUpdateGenre() {
        return Stream.of(
                Arguments.of("1", "Комедия"),
                Arguments.of("3", "История")
        );
    }

    @ParameterizedTest
    @MethodSource("testNotValidUpdateAuthor")
    void testNotValidUpdateGenre(int id, String bookGenre) {

        boolean login = service.login("1", "1");
        // User expectedUser = service.getUserByEmail(email);
        Book book = service.updateAuthor(id, bookGenre);
        assertNull(book);
    }

    static Stream<Arguments> testNotValidUpdateGenre() {
        return Stream.of(
                Arguments.of("1", ""),
                Arguments.of("0", "New Author"),
                Arguments.of("7", "  "),
                Arguments.of("25", "       "),
                Arguments.of("56", "Ужасы")
        );
    }

} // END OF

