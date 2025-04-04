package repository;

import model.Book;
import model.User;
import utils.MyArrayList;
import utils.MyList;

import java.util.concurrent.atomic.AtomicInteger;


public class BookRepositoryImpl implements BookRepository {

    private final MyList<Book> books;
    private final AtomicInteger currentBookId = new AtomicInteger(1);
    // private String author;

    public BookRepositoryImpl() {
        this.books = new MyArrayList<>();
        addStartBooks();
    }

    private void addStartBooks() {
        books.addAll(
                new Book(currentBookId.getAndIncrement(), "Война и Мир", "Лев Николаевич Толстой", "1867", "роман"),
                new Book(currentBookId.getAndIncrement(), "Преступление и наказание", "Федор Михайлович Достоевский", "1866", "роман"),
                new Book(currentBookId.getAndIncrement(), "Мертвые души", "Николай Васильевич Гоголь", "1835", "поэма"),
                new Book(currentBookId.getAndIncrement(), "Вий", "Николай Васильевич Гоголь", "1833", "повесть"),
                new Book(currentBookId.getAndIncrement(), "Властели́н коле́ц", "Толкин, Джон Рональд Руэл", "1954", "роман"),
                new Book(currentBookId.getAndIncrement(), "Над пропастью во ржи", "Сэлинджер, Джером Дэвид", "1951", "роман"),
                new Book(currentBookId.getAndIncrement(), "1984", "Джордж О́руэлл ", "1949", "роман"),
                new Book(currentBookId.getAndIncrement(), "Остров сокровищ", "Роберт Льюис Стивенсон ", "1883", "роман"),
                new Book(currentBookId.getAndIncrement(), "Унесенные ветром", "Маргарет Митчелл ", "1936", "роман"),
                new Book(currentBookId.getAndIncrement(), "Война миров", "Уэллс, Герберт Джордж ", "1897", "научно-фантастический роман"),
                new Book(currentBookId.getAndIncrement(), "Роме́о и Джулье́тта", "Уи́льям Шекспи́р  ", "1594", "трагедия")

        );
    }

    @Override
    public Book addBook(String title, String author, String dateYear, String bookGenre) {
        Book book = new Book(currentBookId.getAndIncrement(), title, author, dateYear, bookGenre);
        books.add(book);
        return book;

    }

    @Override
    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) return book;

        }
        return null;
    }

    @Override
    public MyList<Book> getAllBooks() {
        return books;
    }

    @Override
    public MyList<Book> getAvailableBooks() {
        MyList<Book> availableBooks = new MyArrayList<>(); //  список доступных книг
        for (Book book : books) {
            // if (book.isAvailable()) {
            if (book.getReadingUser() == null) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    @Override
    public MyList<Book> getBorrowedBooks() {
        MyList<Book> borrowedBooks = new MyArrayList<>(); //  список для взятых книг
        for (Book book : books) {
            //if (!book.isAvailable()) {
            if (book.getReadingUser() != null) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }


    @Override
    public MyList<Book> getMyBooks(User user) {
        MyList<Book> myBooks = new MyArrayList<>(); // список книг у пользователя
        for (Book book : books) {
            if (book.getReadingUser() != null && book.getReadingUser().equals(user)) {
                myBooks.add(book);
            }
        }
        return myBooks;
    }


    @Override
    public MyList<Book> getBooksByTitle(String title) {
        MyList<Book> result = new MyArrayList<>();
        // Перебираю список книг, если название совпало (или частично совпало)
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }


    @Override
    public MyList<Book> getBooksByAuthor(String author) {
        MyList<Book> result = new MyArrayList<>();
        // Перебираю авторов книг, если автор совпал (или частично совпало)
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public Book userGetBook(int id, User user) {
        for (Book book : books) {

            if (book.getId() == id) {
                book.setReadingUser(user);
                return book;
            }

        }

        return null;
    }

    @Override
    public boolean isBookExist(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return true;
            }
        }
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
        Book book = getBookById(id);
        int bookIndex = books.indexOf(book);
        return books.remove(bookIndex);
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
