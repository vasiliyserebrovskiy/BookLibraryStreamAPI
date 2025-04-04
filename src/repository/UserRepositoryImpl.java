package repository;

import model.Role;
import model.User;
import utils.MyArrayList;
import utils.MyList;

import java.util.concurrent.atomic.AtomicInteger;

public class UserRepositoryImpl implements UserRepository {

    private final MyList<User> users;
    private final AtomicInteger currenUserId = new AtomicInteger(1);

    public UserRepositoryImpl() {
        this.users = new MyArrayList<>();
        addStartUsers();
    }

    private void addStartUsers() {
        users.addAll(
                new User(currenUserId.getAndIncrement(), "1", "1", Role.ADMIN),
                new User(currenUserId.getAndIncrement(), "2", "2", Role.USER),
                new User(currenUserId.getAndIncrement(), "user2@example.com", "Secure*987", Role.USER),
                new User(currenUserId.getAndIncrement(), "user3@example.com", "TestUser@22", Role.USER),
                new User(currenUserId.getAndIncrement(), "user4@example.com", "Pa$$w0rd99", Role.USER)
        );
    }

    @Override
    public User addUser(String email, String password) {
        User user = new User(currenUserId.getAndIncrement(), email, password, Role.USER);
        users.add(user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUserById(int id) {
        for (User user : users) {
            if (user.getUserId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User updatePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        user.setPassword(newPassword);
        return user;
    }

    @Override
    public boolean deleteUser(String email) {
        User user = getUserByEmail(email);
        return users.remove(user);
    }

    @Override
    public boolean isEmailExist(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MyList<User> getAllUsers() {
        return users;
    }

    @Override
    public User unblockUser(String email) {
        User user = getUserByEmail(email);
        user.setRole(Role.USER);
        return user;
    }

    @Override
    public User unblockUser(int userId) {
        User user = getUserById(userId);
        user.setRole(Role.USER);
        return user;
    }

    @Override
    public User blockUser(String email) {
        User user = getUserByEmail(email);
        user.setRole(Role.BLOCKED);
        return user;
    }

    @Override
    public User blockUser(int id) {
        User user = getUserById(id);
        user.setRole(Role.BLOCKED);
        return user;
    }

    @Override
    public User giveUserAdminRole(int id) {
        User user = getUserById(id);
        if (user != null) {
            user.setRole(Role.ADMIN);
        }
        return user;
    }
}
