package hello.service;

import hello.model.Task;

public interface TaskService {
    Task findById(Long id);
    void save(Task task);
}
