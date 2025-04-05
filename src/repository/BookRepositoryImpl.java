package repository;

import model.Book;
import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class BookRepositoryImpl implements BookRepository {

    private final Map<Integer, Book> books;
    private final AtomicInteger currentBookId = new AtomicInteger(1);
    // private String author;

    public BookRepositoryImpl() {
        this.books = new HashMap<>();
        addStartBooks();
    }

    // Формируем начальный список книг
    private void addStartBooks() {

                addNewBook("Война и Мир", "Лев Николаевич Толстой", "1867", "роман");
                addNewBook("Преступление и наказание", "Федор Михайлович Достоевский", "1866", "роман");
                addNewBook("Мертвые души", "Николай Васильевич Гоголь", "1835", "поэма");
                addNewBook("Вий", "Николай Васильевич Гоголь", "1833", "повесть");
                addNewBook("Властели́н коле́ц", "Толкин, Джон Рональд Руэл", "1954", "роман");
                addNewBook("Над пропастью во ржи", "Сэлинджер, Джером Дэвид", "1951", "роман");
                addNewBook("1984", "Джордж О́руэлл ", "1949", "роман");
                addNewBook("Остров сокровищ", "Роберт Льюис Стивенсон ", "1883", "роман");
                addNewBook("Унесенные ветром", "Маргарет Митчелл ", "1936", "роман");
                addNewBook("Война миров", "Уэллс, Герберт Джордж ", "1897", "научно-фантастический роман");
                addNewBook("Роме́о и Джулье́тта", "Уи́льям Шекспи́р  ", "1594", "трагедия");
    }

    private void addNewBook(String title, String author, String year, String genre) {
        int bookId = currentBookId.getAndIncrement();
        books.put(bookId, new Book(bookId, title,author,year,genre));
    }

    @Override
    public Book addBook(String title, String author, String dateYear, String bookGenre) {
        int bookId = currentBookId.getAndIncrement();
        Book book = new Book(bookId, title, author, dateYear, bookGenre);
        books.put(bookId, book);
        return book;

    }

    @Override
    public Book getBookById(int id) {
        return books.get(id);
//        for (Book book : books) {
//            if (book.getId() == id) return book;
//
//        }
//        return null;
    }

    @Override
    public List<Book> getAllBooks() {
            return books.values().stream()
                    .sorted(Comparator.comparing(Book::getId))
                    .collect(Collectors.toList());
        //return books;
    }

    @Override
    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(book -> book.getReadingUser() == null)
                .collect(Collectors.toList());
//        MyList<Book> availableBooks = new MyArrayList<>(); //  список доступных книг
//        for (Book book : books) {
//            // if (book.isAvailable()) {
//            if (book.getReadingUser() == null) {
//                availableBooks.add(book);
//            }
//        }
//        return availableBooks;
    }

    @Override
    public List<Book> getBorrowedBooks() {
        return books.values().stream()
                .filter(book -> book.getReadingUser() != null)
                .collect(Collectors.toList());

//        MyList<Book> borrowedBooks = new MyArrayList<>(); //  список для взятых книг
//        for (Book book : books) {
//            //if (!book.isAvailable()) {
//            if (book.getReadingUser() != null) {
//                borrowedBooks.add(book);
//            }
//        }
//        return borrowedBooks;
    }


    @Override
    public List<Book> getMyBooks(User user) {
            return books.values().stream()
                    .filter(book -> book.getReadingUser() != null)
                    .filter(book -> book.getReadingUser().equals(user))
                    .collect(Collectors.toList());
//        MyList<Book> myBooks = new MyArrayList<>(); // список книг у пользователя
//        for (Book book : books) {
//            if (book.getReadingUser() != null && book.getReadingUser().equals(user)) {
//                myBooks.add(book);
//            }
//        }
//        return myBooks;
    }


    @Override
    public List<Book> getBooksByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());

//        MyList<Book> result = new MyArrayList<>();
//        // Перебираю список книг, если название совпало (или частично совпало)
//        for (Book book : books) {
//            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
//                result.add(book);
//            }
//        }
//        return result;
    }


    @Override
    public List<Book> getBooksByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());

//        MyList<Book> result = new MyArrayList<>();
//        // Перебираю авторов книг, если автор совпал (или частично совпало)
//        for (Book book : books) {
//            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
//                result.add(book);
//            }
//        }
//        return result;
    }

    @Override
    public Book userGetBook(int id, User user) {
        Book book = getBookById(id);
        book.setReadingUser(user);
        return book;
//        for (Book book : books) {
//            if (book.getId() == id) {
//                book.setReadingUser(user);
//                return book;
//            }
//        }
//        return null;
    }

    @Override
    public boolean isBookExist(int id) {
        Book book = getBookById(id);
        if (book != null) {
            return true;
        }
//        for (Book book : books) {
//            if (book.getId() == id) {
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public Book userReturnBook(int id) {
        Book book = getBookById(id);
        book.setReadingUser(null);
        return book;
    }

    @Override
    public Book deleteBookById(int id) {
        return books.remove(id);
//        Book book = getBookById(id);
//        int bookIndex = books.indexOf(book);
//        return books.remove(bookIndex);
    }

    @Override
    public Book updateTitle(int id, String title) {
        Book book = getBookById(id);
        book.setTitle(title);
        return book;
    }

    @Override
    public Book updateAuthor(int id, String author) {
        Book book = getBookById(id);
        book.setAuthor(author);
        return book;
    }

    @Override
    public Book updateDateYear(int id, String dateYear) {
        Book book = getBookById(id);
        book.setDateYear(dateYear);
        return book;
    }

    @Override
    public Book updateGenre(int id, String bookGenre) {
        Book book = getBookById(id);
        book.setBookGenre(bookGenre);
        return book;
    }

    @Override
    public User getReadingUser(int id) {
        Book book = getBookById(id);
        return book.getReadingUser();
    }
}
