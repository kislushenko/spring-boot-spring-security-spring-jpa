package hello.service.impl;

import hello.dao.ProjectDao;
import hello.model.Project;
import hello.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;


    @Override
    public Project findById(Long id) throws EntityNotFoundException  {
        return projectDao.getOne(id);
    }

    @Override
    public void save(Project project) {
        projectDao.save(project);
    }
}
