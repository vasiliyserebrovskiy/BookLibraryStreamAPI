package repository;

import model.User;
import utils.MyList;

public interface UserRepository {
    //CRUD
    User addUser(String email, String password); // При реализации не забыть генерировать уникальный id первым параметров!

    User getUserByEmail(String email);

    User getUserById(int id);

    MyList<User> getAllUsers(); // Для отображения всех зарегистрированных пользователей

    User login(String email, String password);

    User updatePassword(String email, String newPassword);

    boolean deleteUser(String email); // Только ADMIN

    User giveUserAdminRole(int id);

    boolean isEmailExist(String email);

    User unblockUser(String email);

    User unblockUser(int userId);

    User blockUser(String email);

    User blockUser(int id);

}
