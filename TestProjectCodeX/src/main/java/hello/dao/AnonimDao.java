package hello.dao;

import hello.model.Anonim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonimDao extends JpaRepository<Anonim, Long> {
    Anonim findByName(String name);
    void deleteById(Long id);
}
