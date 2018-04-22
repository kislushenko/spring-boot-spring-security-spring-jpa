package hello.service.impl;

import hello.dao.RoleDao;
import hello.model.Role;
import hello.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceDao implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public Role findById(Long id) {
        return roleDao.getOne(id);
    }
}
