package hello.service;

import hello.model.Anonim;
import hello.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface RegistrationService {
    void save(User user, Long role);
    Anonim findByName(String name);
    void deleteById(Long id);
}
