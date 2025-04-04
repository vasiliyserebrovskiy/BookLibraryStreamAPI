import repository.BookRepository;
import repository.BookRepositoryImpl;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import service.MainService;
import service.MainServiceImpl;
import view.Menu;

public class BooksLibraryApplication {
    public static void main(String[] args) {

        BookRepository bookRepository = new BookRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();

        MainService service = new MainServiceImpl(bookRepository, userRepository);

        Menu menu = new Menu(service);

        menu.start();

    }
}
