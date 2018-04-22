package hello.service.impl;

import hello.dao.TaskDao;
import hello.model.Task;
import hello.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao taskDao;

    @Override
    public Task findById(Long id) {
        return taskDao.getOne(id);
    }

    @Override
    public void save(Task task) {
        taskDao.save(task);
    }
}
