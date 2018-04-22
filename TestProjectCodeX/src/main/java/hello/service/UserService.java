package hello.service;

import hello.model.User;

import java.util.Set;

public interface UserService {
    void saveeasy(User user);
    void save(User user);
    User findByUsername(String username);
    Set<User> findAllByFNameAndLName(String fname, String lname);
    User findById(Long id);
}
