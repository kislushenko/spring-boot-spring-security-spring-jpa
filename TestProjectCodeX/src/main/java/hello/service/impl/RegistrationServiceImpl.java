package hello.service.impl;

import hello.dao.AnonimDao;
import hello.dao.RoleDao;
import hello.model.Anonim;
import hello.model.User;
import hello.service.RegistrationService;
import hello.service.UserService;
import hello.service.impl.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import java.util.Random;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private AnonimDao anonimDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;
    @Autowired
    public MailSender mailSender;

    @Override
    public void save(User user, Long role){
        String code = generateString(10);
        String body = "http://localhost:8080/activation?name=" + user.getUsername() + "&code=" + code;
        mailSender.sendMail("testforcodex@gmail.com", user.getUsername(), "Подтверждение регистрации", body);
        user.setRoles(roleDao.getOne(3L));
        userService.save(user);
        Anonim anonim = new Anonim();
        anonim.setName(user.getUsername());
        anonim.setCode(code);
        anonim.setRole(role);
        anonimDao.save(anonim);
    }

    @Override
    public Anonim findByName(String name) {
        return anonimDao.findByName(name);
    }

    @Override
    public void deleteById(Long id) {
        anonimDao.deleteById(id);
    }

    public static String generateString(int length)
    {
        String characters = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOASDFGHJKLZXCVBNM";
        Random rnd = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rnd.nextInt(characters.length()));
        }
        return new String(text);
    }
}
