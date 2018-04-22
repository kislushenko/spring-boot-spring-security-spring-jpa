package hello.dao;

import hello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface UserDao extends JpaRepository<User, Long>{
    User findByUsername(String username);
    Set<User> findAllByFnameAndLname(String fname, String lname);
    void deleteById(Long id);
}
