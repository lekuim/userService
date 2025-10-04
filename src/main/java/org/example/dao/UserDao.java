package org.example.dao;
import org.example.entity.User;

import java.util.List;

public interface UserDao {
    void create(User user);
    User readById(Long id);
    List<User> readAll();
    void update(User user);
    void delete(Long id);
}
