package view;

import model.Book;
import model.Role;
import model.User;
import service.MainService;
import utils.MyList;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private final MainService service;
    private Scanner scanner = new Scanner(System.in);
    //for warning and error printing
    public static final String COLOR_RED = "\u001B[31m";
    public static final String COLOR_YELLOW = "\u001B[33m";
    public static final String COLOR_RESET = "\u001B[0m";
    public static final String COLOR_GREEN = "\u001B[32m";

    public Menu(MainService service) {
        this.service = service;
    }

    public void start() {
        showMainMenu();
    }

    private void showMainMenu() {
        while (true) {

            System.out.println("\nДобро пожаловать в библиотеку \"Знание Века\"");
            System.out.println("1 Меню книги");
            System.out.println("2 Меню пользователя");
            System.out.println("3 Меню администратора");
            System.out.println("0 Выход");
            System.out.println("\nВыберите номер пункта меню");

            String input = scanner.nextLine();

            if (isInteger(input)) {
                int choice = Integer.parseInt(input);

                if (choice == 0) {
                    printOkMessage("\nДо свидания!");
                    // Завершение работы приложения
                    System.exit(0);
                }

                showSubMenu(choice);
            } else {
                printErrorMessage("\nВы ввели не число!");
                waitRead();
            }
        }
    }

    private void showSubMenu(int choice) {
        switch (choice) {
            case 1:
                showBookMenu();
                break;
            case 2:
                showUserMenu();
                break;
            case 3:
                showAdminMenu();
                break;
            default:
                printWarningMessage("\nСделайте корректный выбор");
                waitRead();
        }
    }

    // Меню книг
    private void showBookMenu() {
        while (true) {

            if (service.getActiveUser() == null) {
                System.out.println("\nМеню книг:");
                System.out.println("1 Список всех книг");
                System.out.println("2 Поиск книги по названию");
                System.out.println("3 Поиск книги по автору");
                System.out.println("0 Выход в предыдущее меню");
                System.out.println("\nВыберите номер пункта меню");

            } else {
                System.out.println("\nМеню книг:");
                System.out.println("1 Список всех книг");
                System.out.println("2 Поиск книги по названию");
                System.out.println("3 Поиск книги по автору");
                System.out.println("4 Список всех свободных книг");
                System.out.println("5 Список всех взятых книг");
                System.out.println("6 Список моих книг");
                System.out.println("7 Взять книгу");
                System.out.println("8 Вернуть книгу");
                System.out.println("9 Кто читает книгу");
                System.out.println("0 Выйти в предыдущее меню");
                System.out.println("\nВыберите номер пункта меню");
            }

            String input = scanner.nextLine();

            if (isInteger(input)) {
                int choice = Integer.parseInt(input);

                if (choice == 0) break;

                //Проверка ввода если пользователь не авторизован
                if (service.getActiveUser() == null && choice > 3) {
                    printWarningMessage("\nСделайте корректный выбор");
                    waitRead();
                    continue;
                }

                handleBookMenuInput(choice);

            } else {
                printErrorMessage("\nВы ввели не число!");
                waitRead();
            }

        }
    }

    private void handleBookMenuInput(int input) {
        MyList<Book> books;
        String bookStrId;

        switch (input) {
            case 1:
                books = service.getAllBooks();
                showBooksList(books);
                waitRead();
                break;
            case 2:
                System.out.println("Введите название книги:");
                String bookTitle = scanner.nextLine();
                books = service.getBooksByTitle(bookTitle);
                if (books.size() == 0) {
                    printWarningMessage("\nКниг с таким названием в нашей библиотеке не найдено.");
                    waitRead();
                } else {
                    showBooksList(books);
                    waitRead();
                }
                break;
            case 3:
                System.out.println("Введите автора книги:");
                String bookAuthor = scanner.nextLine();
                books = service.getBooksByAuthor(bookAuthor);
                if (books.size() == 0) {
                    printWarningMessage("\nКниг этого автора в нашей библиотеке не найдено.");
                    waitRead();
                } else {
                    showBooksList(books);
                    waitRead();
                }
                break;
            case 4:
                books = service.getAvailableBooks();
                if (books.size() == 0) {
                    printWarningMessage("\nСвободных книг в нашей библиотеке на данный момент нет.");
                    waitRead();
                } else {
                    showBooksList(books);
                    waitRead();
                }
                break;
            case 5:
                books = service.getBorrowedBooks();
                if (books.size() == 0) {
                    printWarningMessage("\nВсе книги в нашей библиотеке на данный момент доступны.");
                    waitRead();
                } else {
                    showBooksList(books);
                    waitRead();
                }
                break;
            case 6:
                books = service.getMyBooks();
                if (books.size() == 0) {
                    printWarningMessage("\nУ вас нет книг на руках.");
                    waitRead();
                } else {
                    showBooksList(books);
                    waitRead();
                }
                break;
            case 7:
                System.out.println("Введите id книги, которую Вы хотите взять:");
                bookStrId = scanner.nextLine();

                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    Book book = service.userGetBook(bookIntId);
                    if (book == null) {
                        printWarningMessage("\nНе удалось взять книгу с id:" + bookStrId);
                        printWarningMessage("Книги с таким id нет, ее уже кто-то читает или вы заблокированы.");
                        waitRead();
                    } else {
                        System.out.println("\nВы успешно взяли книгу: " + book);
                        waitRead();
                    }
                } else {
                    printErrorMessage("\nВы ввели не корректный id книги");
                    waitRead();
                }
                break;
            case 8:
                System.out.println("Введите id книги, которую Вы хотите вернуть:");
                bookStrId = scanner.nextLine();

                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    Book book = service.userReturnBook(bookIntId);
                    if (book == null) {
                        printWarningMessage("\nНе удалось вернуть книгу с id:" + bookStrId);
                        printWarningMessage("Книги с таким id нет или Вы ее не читаете.");
                        waitRead();
                    } else {
                        System.out.println("\nВы успешно вернули книгу: " + book);
                        waitRead();
                    }
                } else {
                    printErrorMessage("\nВы ввели не корректный id книги.");
                    waitRead();
                }
                break;
            case 9:
                System.out.println("Чтобы узнать кто читает книгу, введите id книги:");
                bookStrId = scanner.nextLine();

                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    User user = service.whoReadBook(bookIntId);
                    if (user == null) {
                        printWarningMessage("\nНе найдено книги с id или ее никто не читает: " + bookIntId);
                        waitRead();
                    } else {
                        printOkMessage("Книгу с id:" + bookIntId + " читает " + user);
                        waitRead();
                    }
                } else {
                    printErrorMessage("\nВы ввели не корректный id книги.");
                    waitRead();
                }
                break;
            default:
                printWarningMessage("\nСделайте корректный выбор");
                waitRead();
        }
    }

    //Меню пользователя
    private void showUserMenu() {
        while (true) {

            if (service.getActiveUser() == null) {
                System.out.println("\nМеню пользователя:");
                System.out.println("1 Вход в систему");
                System.out.println("2 Зарегистрироваться");
                System.out.println("0 Выйти в предыдущее меню");
                System.out.println("\nВыберите номер пункта меню");
            } else {
                System.out.println("\nМеню пользователя:");
                System.out.println("1 Вход в систему");
                System.out.println("2 Зарегистрироваться");
                System.out.println("3 Изменить пароль");
                System.out.println("4 Выйти");
                System.out.println("0 Выйти в предыдущее меню");
                System.out.println("\nВыберите номер пункта меню");
            }

            String input = scanner.nextLine();

            if (isInteger(input)) {
                int choice = Integer.parseInt(input);

                if (choice == 0) break;

                //Проверка ввода если пользователь не авторизован
                if (service.getActiveUser() == null && choice > 2) {
                    printWarningMessage("\nСделайте корректный выбор");
                    waitRead();
                    continue;
                }

                handleUserMenuInput(choice);
            } else {
                printErrorMessage("\nВы ввели не число!");
                waitRead();
            }

        }
    }

    private void handleUserMenuInput(int input) {
        switch (input) {
            case 1:
                if (service.getActiveUser() == null) {
                    System.out.println("Введине ваш email:");
                    String email = scanner.nextLine();
                    System.out.println("Введите ваш пароль:");
                    String password = scanner.nextLine();
                    boolean isLogin = service.login(email, password);

                    if (isLogin) {
                        printOkMessage("\nВы успешно вошли в систему.");
                        waitRead();
                    } else {
                        printErrorMessage("\nНе удалось войти в систему.");
                        printWarningMessage("Вы не зарегистрированы в системе или");
                        printWarningMessage("Вы ввели неверный email или пароль.");
                        waitRead();
                    }
                } else {
                    printWarningMessage("Вы уже вошли в систему");
                    waitRead();
                }
                break;

            case 2:
                if (service.getActiveUser() != null) {
                    printWarningMessage("\nВы уже зарегистрированы и вошли в систему.");
                    waitRead();
                } else {
                    System.out.println("Пожалуйста введите ваш email:");
                    String email = scanner.nextLine();
                    System.out.println("Введите ваш пароль:");
                    String password = scanner.nextLine();
                    User user = service.createUser(email, password);
                    if (user == null) {
                        printErrorMessage("\nК сожалению, попытка Вышей регистрации не удалась.");
                        printErrorMessage("Пользователь с таким E-mail уже зарегистрирован или");
                        printErrorMessage("указанный Вами логин или пароль не соответствуют требованиям нашей системы!");
                        printErrorMessage("---------- Наши требования для e-mail и пароля ----------");
                        printOkMessage("E-mail должен быть указан в формате: myname@gmail.com");
                        printOkMessage("Пароль должен удовлетворять следующим критериям:");
                        printOkMessage("1 должен содержать не менее 8 символов");
                        printOkMessage("2 должен содержать минимум одну маленькую букву");
                        printOkMessage("3 должен содержать минимум одну большую букву");
                        printOkMessage("4 должен содержать хотя бы одну цифру");
                        printOkMessage("5 должен содержать хотя бы один спец символ из перечисленных: \"!#%$@&*()[],.-\"");
                        printOkMessage("Пожалуйста, повторите попытку регистрации.");
                        waitRead();
                    } else {
                        printOkMessage("\nПоздравляем, Вы успешно зарегистрировались в нашей библиотеке.");
                        waitRead();
                    }
                }
                break;
            case 3:
                System.out.println("\nПожалуйста введите новый пароль:");
                String password = scanner.nextLine();
                User user = service.updatePassword(password);
                if (user == null) {
                    printErrorMessage("К сожалению, указанный вами пароль не соответствует требованиям нашей системы!");
                    printErrorMessage("---------- Наши требования паролю ----------");
                    printOkMessage("Пароль должен удовлетворять следующим критериям:");
                    printOkMessage("1 должен содержать не менее 8 символов");
                    printOkMessage("2 должен содержать минимум одну маленькую букву");
                    printOkMessage("3 должен содержать минимум одну большую букву");
                    printOkMessage("4 должен содержать хотя бы одну цифру");
                    printOkMessage("5 должен содержать хотя бы один спец символ из перечисленных: \"!#%$@&*()[],.-\"");
                    printOkMessage("Пожалуйста, повторите попытку регистрации.");
                    waitRead();
                } else {
                    printOkMessage("\nВы успешно изменили свой пароль.");
                    waitRead();
                }
                break;
            case 4:
                boolean isLogout = service.logout();
                if (isLogout) {
                    printOkMessage("\nВы успешно вышли из системы,");
                } else {
                    printErrorMessage("\nНе удалось выполнить выход.");
                }
                break;
            default:
                printWarningMessage("\nСделайте корректный выбор");
                waitRead();
        }
    }

    // Меню админа
    private void showAdminMenu() {
        while (true) {
            if (service.getActiveUser() != null) {
                if (service.getActiveUser().getRole() == Role.ADMIN) {
                    System.out.println("\nМеню администратора");
                    System.out.println("1 Добавить книгу");
                    System.out.println("2 Удалить книгу");
                    System.out.println("3 Редактировать информацию о книге");
                    System.out.println("4 Разблокировать пользователя");
                    System.out.println("5 Заблокировать пользователя");
                    System.out.println("6 Дать пользователю права администратора");
                    System.out.println("7 Список всех пользователей");
                    System.out.println("8 Удалить пользователя");
                    System.out.println("0 Выйти в предыдущее меню");
                    System.out.println("\nВыберите номер пункта меню");
                } else {
                    printWarningMessage("\nПростите, Вы не являетесь администратором.");
                    waitRead();
                    break;
                }

            } else {
                printWarningMessage("\nПростите, Вы не вошли в систему.");
                waitRead();
                break;
            }

            String input = scanner.nextLine();

            if (isInteger(input)) {
                int choice = Integer.parseInt(input);

                if (choice == 0) break;

                handleAdminMenuInput(choice);
            } else {
                printErrorMessage("\nВы ввели не число!");
                waitRead();
            }

        }
    }

    private void handleAdminMenuInput(int input) {
        Book book;
        User user;
        String choice;
        switch (input) {
            case 1:
                System.out.println("Введите название книги:");
                String title = scanner.nextLine();
                System.out.println("Введите автора книги:");
                String author = scanner.nextLine();
                System.out.println("Введите год печати книги:");
                String dateYear = scanner.nextLine();
                System.out.println("Введите жанр книги:");
                String bookGenre = scanner.nextLine();
                book = service.createBook(title, author, dateYear, bookGenre);
                if (book == null) {
                    printWarningMessage("\nК сожалению не удалось завести книгу.");
                    printWarningMessage("Был не указан один из параметров книги или параметры книги были заданы не верно.");
                    waitRead();
                } else {
                    printOkMessage("Новая книга успешно заведена: " + book);
                    waitRead();
                }
                break;
            case 2:
                System.out.println("Введите id книги, которую вы хотите удалить:");
                String bookStrId = scanner.nextLine();
                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    book = service.deleteBookById(bookIntId);
                    if (book == null) {
                        printErrorMessage("Не удалось удалить книгу с id " + bookIntId);
                    } else {
                        printOkMessage("Вы успешно удалили книгу: " + book);
                    }
                    waitRead();
                } else {
                    printErrorMessage("Вы ввели некорректный id.");
                    waitRead();
                }
                break;
            case 3:
                showEditBookMenu();
                break;

            case 4:
                System.out.println("Введите 1 для разблокировки по email или 2 для разблокировки по id:");
                choice = scanner.nextLine();
                if ("1".equals(choice)) {
                    System.out.println("Введите email для разблокировки:");
                    String email = scanner.nextLine();
                    user = service.unblockUser(email);
                    if (user != null) {
                        printOkMessage("Пользователь с email " + email + " успешно разблокирован.");
                    } else {
                        printErrorMessage("Не удалось разблокировать пользователя.");
                    }
                } else if ("2".equals(choice)) {
                    System.out.println("Введите id пользователя для разблокировки:");
                    String inputStrId = scanner.nextLine();
                    if (isInteger(inputStrId)) {
                        int userId = Integer.parseInt(inputStrId);
                        user = service.unblockUser(userId);
                        if (user != null) {
                            printOkMessage("Пользователь с id " + userId + " успешно разблокирован.");
                        } else {
                            printErrorMessage("Вы ввели некорректный id.");
                        }
                    } else {
                        printErrorMessage("Вы ввели некорректный id.");
                    }
                } else {
                    printErrorMessage("Неверный выбор.");
                }
                waitRead();
                break;

            case 5:
                System.out.println("Введите 1 для блокировки по email или 2 для блокировки по id: ");
                choice = scanner.nextLine();
                if ("1".equals(choice)) {
                    System.out.println("Введите email для блокировки:");
                    String email = scanner.nextLine();
                    user = service.getUserByEmail(email);
                    if (user.getRole() == Role.BLOCKED) {
                        printWarningMessage("Пользователь c email " + email + " уже заблокирован.");
                        waitRead();
                    } else {
                        user = service.blockUser(email);
                        if (user != null) {
                            printOkMessage("Пользователь с email " + email + " успешно заблокирован.");
                        } else {
                            printErrorMessage("Вы ввели некорректный email.");
                        }
                    }
                } else if ("2".equals(choice)) {
                    System.out.println("Введите id пользователя для блокировки:");
                    String inputStrId = scanner.nextLine();
                    if (isInteger(inputStrId)) {
                        int userId = Integer.parseInt(inputStrId);
                        user = service.getUserById(userId);
                        if (user.getRole() == Role.BLOCKED) {
                            printWarningMessage("Пользователь с id " + userId + " уже заблокирован.");
                        } else {
                            user = service.blockUser(userId);
                            if (user != null) {
                                printOkMessage("Пользователь с id " + userId + " успешно заблокирован.");
                            } else {
                                printErrorMessage("Не удалось заблокировать пользователя.");
                            }
                        }
                    } else {
                        printErrorMessage("Вы ввели некорректный id.");
                    }
                } else {
                    printErrorMessage("Неверный выбор.");
                }
                waitRead();
                break;

            case 6:
                System.out.println("Введите id пользователя для назначения прав администратора: ");
                String inputStrId = scanner.nextLine();
                if (isInteger(inputStrId)) {
                    int userId = Integer.parseInt(inputStrId);
                    user = service.giveUserAdminRole(userId);
                    if (user != null) {
                        printOkMessage("Пользователю с id " + userId + " успешно даны права администратора.");
                    } else {
                        printErrorMessage("Вы ввели некорректный id.");
                    }
                    waitRead();
                } else {
                    printErrorMessage("Вы ввели некорректный id.");
                    waitRead();
                }
                break;

            case 7:
                showUsersList(service.getAllUsers());
                waitRead();
                break;
            case 8:
                System.out.println("Введите email пользователя для удаления: ");
                String email = scanner.nextLine();
                user = service.deleteUser(email);
                if (user != null) {
                    printOkMessage("Пользователь с email " + email + " успешно удален. ");
                } else {
                    printErrorMessage("Не удалось найти пользователя. ");
                }
                waitRead();
                break;
            default:
                printWarningMessage("\nСделайте корректный выбор");
                waitRead();
        }
    }

    private void showEditBookMenu() {
        while (true) {
            System.out.println("\nМеню изменения данных о книге:");
            System.out.println("1 Название книги");
            System.out.println("2 Автора книги");
            System.out.println("3 Год печати книги");
            System.out.println("4 Жанр книги");
            System.out.println("0 Выход в предыдущее меню");
            System.out.println("\nВыберите номер пункта меню");

            String input = scanner.nextLine();

            if (isInteger(input)) {
                int choice = Integer.parseInt(input);

                if (choice == 0) break;

                handleEditBookMenuInput(choice);
            } else {
                printErrorMessage("\nВы ввели не число!");
                waitRead();
            }
        }
    }

    private void handleEditBookMenuInput(int input) {
        Book book;
        String bookStrId;
        switch (input) {
            case 1:
                System.out.println("\nВведите id книги:");
                bookStrId = scanner.nextLine();
                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    System.out.println("Введите название книги:");
                    String title = scanner.nextLine();
                    book = service.updateTitle(bookIntId, title);
                    if (book == null) {
                        printErrorMessage("Вы указали неверное id, некорректное название книги или книгу читают.");
                        waitRead();
                    } else {
                        printOkMessage("Вы успешно изменили название книги: " + book);
                        waitRead();
                    }
                } else {
                    printErrorMessage("Вы ввели некорректный id.");
                    waitRead();
                }
                break;
            case 2:
                System.out.println("\nВведите id книги:");
                bookStrId = scanner.nextLine();
                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    System.out.println("Введите автора книги:");
                    String author = scanner.nextLine();
                    book = service.updateAuthor(bookIntId, author);
                    if (book == null) {
                        printErrorMessage("Вы указали неверное id, некорректного автора книги или книгу читают.");
                        waitRead();
                    } else {
                        printOkMessage("Вы успешно изменили автора книги: " + book);
                        waitRead();
                    }
                } else {
                    printErrorMessage("Вы ввели некорректный id.");
                    waitRead();
                }
                break;
            case 3:
                System.out.println("\nВведите id книги:");
                bookStrId = scanner.nextLine();
                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    System.out.println("Введите год издания книги:");
                    String dateYear = scanner.nextLine();
                    book = service.updateDateYear(bookIntId, dateYear);
                    if (book == null) {
                        printErrorMessage("Вы указали неверное id, некорректный год издания книги или книгу читают.");
                        waitRead();
                    } else {
                        printOkMessage("Вы успешно изменили год издания книги: " + book);
                        waitRead();
                    }
                } else {
                    printErrorMessage("Вы ввели некорректный id.");
                    waitRead();
                }
                break;
            case 4:
                System.out.println("\nВведите id книги:");
                bookStrId = scanner.nextLine();
                if (isInteger(bookStrId)) {
                    int bookIntId = Integer.parseInt(bookStrId);
                    System.out.println("Введите жанр книги:");
                    String bookGenre = scanner.nextLine();
                    book = service.updateGenre(bookIntId, bookGenre);
                    if (book == null) {
                        printErrorMessage("Вы указали неверное id, некорректный жанр книги или книгу читают.");
                        waitRead();
                    } else {
                        printOkMessage("Вы успешно изменили жанр книги: " + book);
                        waitRead();
                    }
                } else {
                    printErrorMessage("Вы ввели некорректный id.");
                    waitRead();
                }
                break;
            default:
                printWarningMessage("\nСделайте корректный выбор");
                waitRead();
        }

    }

    // Печать книг
    private void showBooksList(MyList<Book> books) {
        for (Book book : books) {
            System.out.printf("Id: %d | Название: %s | Автор: %s | Год издания: %s | Жанр: %s\n", book.getId(), book.getTitle(), book.getAuthor(), book.getDateYear(), book.getBookGenre());
        }
    }

    // Печать пользователей
    private void showUsersList(List<User> users) {
        for (User user : users) {
            System.out.printf("Id: %d | E-mail: %s | Роль: %s\n", user.getUserId(), user.getEmail(), user.getRole());
        }
    }

    // Задержка в меню
    private void waitRead() {
        System.out.println("\nДля продолжения нажмите Enter");
        scanner.nextLine();
    }

    // Проверяем что пользователь ввел число
    private boolean isInteger(String str) {
        return str.matches("\\d+"); // Только положительные целые числа
    }

    //Печать текста ошибки
    private void printErrorMessage(String string) {
        System.out.println(COLOR_RED + string + COLOR_RESET);
    }

    //Печать текста предупреждения
    private void printWarningMessage(String string) {
        System.out.println(COLOR_YELLOW + string + COLOR_RESET);
    }

    // Печать успешного сообщения
    private void printOkMessage(String string) {
        System.out.println(COLOR_GREEN + string + COLOR_RESET);
    }

} // END CLASS MENU
