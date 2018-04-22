package hello.service.impl;

import hello.dao.CommentDao;
import hello.model.Comment;
import hello.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public Comment findById(Long id) {
        return commentDao.getOne(id);
    }

    @Override
    public void save(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentDao.deleteById(id);
    }
}
