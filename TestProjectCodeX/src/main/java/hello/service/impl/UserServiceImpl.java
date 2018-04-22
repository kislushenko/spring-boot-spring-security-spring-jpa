package hello.service.impl;

import hello.dao.RoleDao;
import hello.dao.UserDao;
import hello.model.User;
import hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveeasy(User user) {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRoles(roleDao.getOne(3L));
        userDao.save(user);
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Set<User> findAllByFNameAndLName(String fname, String lname) {
        return userDao.findAllByFnameAndLname(fname, lname);
    }

    @Override
    public User findById(Long id) {
        return userDao.getOne(id);
    }


}
