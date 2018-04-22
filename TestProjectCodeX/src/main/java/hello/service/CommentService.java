package hello.service;

import hello.model.Comment;

public interface CommentService {
    Comment findById(Long id);
    void save(Comment comment);
    void deleteById(Long id);
}
