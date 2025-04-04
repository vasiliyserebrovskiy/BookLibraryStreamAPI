package repository;

import model.User;
import utils.MyList;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    //CRUD
    User addUser(String email, String password); // При реализации не забыть генерировать уникальный id первым параметров!

    User getUserByEmail(String email);

    Optional<User> getUserById(int id);

    List<User> getAllUsers(); // Для отображения всех зарегистрированных пользователей

    User login(String email, String password);

    User updatePassword(String email, String newPassword);

    User deleteUser(String email); // Только ADMIN

    Optional<User> giveUserAdminRole(int userId);

   // User isEmailExist(String email);

    User unblockUser(String email);

    Optional<User> unblockUser(int userId);

    User blockUser(String email);

    Optional<User> blockUser(int userId);

}
