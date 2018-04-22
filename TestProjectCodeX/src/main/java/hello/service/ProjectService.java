package hello.service;

import hello.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectService {
    Project findById(Long id);
    void save(Project project);
}
