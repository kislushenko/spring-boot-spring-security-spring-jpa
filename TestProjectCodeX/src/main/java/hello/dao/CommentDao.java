package hello.dao;

import hello.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, Long> {
    void deleteById(Long id);
}
