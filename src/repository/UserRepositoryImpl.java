package repository;

import model.Role;
import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> users;
    private final AtomicInteger currenUserId = new AtomicInteger(1);

    public UserRepositoryImpl() {
        this.users = new HashMap<>();
        addStartUsers();
    }

    private void addStartUsers() {
        Map<String, User> initialUsers = Map.of(
                "1", new User(currenUserId.getAndIncrement(), "1", "1", Role.ADMIN),
                "2", new User(currenUserId.getAndIncrement(), "2", "2", Role.USER),
                "user2@example.com", new User(currenUserId.getAndIncrement(), "user2@example.com", "Secure*987", Role.USER),
                "user3@example.com", new User(currenUserId.getAndIncrement(), "user3@example.com", "TestUser@22", Role.USER),
                "user4@example.com",new User(currenUserId.getAndIncrement(), "user4@example.com", "Pa$$w0rd99", Role.USER)
        );

        users.putAll(initialUsers);
//        users.addAll(
//                new User(currenUserId.getAndIncrement(), "1", "1", Role.ADMIN),
//                new User(currenUserId.getAndIncrement(), "2", "2", Role.USER),
//                new User(currenUserId.getAndIncrement(), "user2@example.com", "Secure*987", Role.USER),
//                new User(currenUserId.getAndIncrement(), "user3@example.com", "TestUser@22", Role.USER),
//                new User(currenUserId.getAndIncrement(), "user4@example.com", "Pa$$w0rd99", Role.USER)
//        );
    }

    @Override
    public User addUser(String email, String password) {
        User user = new User(currenUserId.getAndIncrement(), email, password, Role.USER);
        users.put(email, user);
        //users.add(user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return users.get(email);
//        for (User user : users) {
//            if (user.getEmail().equalsIgnoreCase(email)) {
//                return user;
//            }
//        }
//        return null;
    }

    @Override
    public Optional<User> getUserById(int id) {
        // ищем в потоке по значениям ->если не нашли возвращаем null.
        return users.values().stream()
                .filter(user -> user.getUserId() == id)
                .findFirst();

//        for (User user : users) {
//            if (user.getUserId() == id) {
//                return user;
//            }
//        }
//        return null;
    }

    @Override
    public User login(String email, String password) {

        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;

//        for (User user : users) {
//            if (user.getEmail().equalsIgnoreCase(email)
//                    && user.getPassword().equals(password)) {
//                return user;
//            }
//        }
//        return null;
    }

    @Override
    public User updatePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        user.setPassword(newPassword);
        return user;
    }

    @Override
    public User deleteUser(String email) {
        return users.remove(email);
//        User user = getUserByEmail(email);
//        return users.remove(user);
    }

//    @Override
//    public User isEmailExist(String email) {
//        return users.get(email);
////        for (User user : users) {
////            if (user.getEmail().equalsIgnoreCase(email)) {
////                return true;
////            }
////        }
////        return false;
//    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream()
                .sorted(Comparator.comparing(User::getUserId))
                .collect(Collectors.toList());
        //return new ArrayList<>(users.values());
    }

    @Override
    public User unblockUser(String email) {
        User user = getUserByEmail(email);
        user.setRole(Role.USER);
        return user;
    }

    @Override
    public Optional<User> unblockUser(int userId) {
        Optional<User> user = getUserById(userId);
        user.get().setRole(Role.USER); //не делаем тут проверок, пользователь обязательно есть
        return user;
    }

    @Override
    public User blockUser(String email) {
        User user = getUserByEmail(email);
        user.setRole(Role.BLOCKED);
        return user;
    }

    @Override
    public Optional<User> blockUser(int userId) {
        Optional<User> user = getUserById(userId);
        user.get().setRole(Role.BLOCKED);
        return user;
    }

    @Override
    public Optional<User> giveUserAdminRole(int userId) {
        Optional<User> user = getUserById(userId);
        user.get().setRole(Role.ADMIN);

        return user;
    }
}
