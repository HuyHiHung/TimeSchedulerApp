package dao;

import model.User;
import java.util.List;

public interface UserDAO {
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    User getUserById(int userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean checkLogin(String username, String password);
}
