package hello.controller;

import com.sun.mail.smtp.SMTPAddressFailedException;
import hello.model.*;
import hello.service.*;
import hello.service.impl.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Controller
public class GreetingController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    public MailSender mailSender;

    @GetMapping("/project")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        User user = userService.findByUsername(securityService.getCurrentUsername());
        Set<Project> projects;
        if(user.getRoles().getName().equals("ROLE_MANAGER")) {
            projects = user.getProjects_m();
            model.addAttribute("text", "Ваши проекты:");
            model.addAttribute("project", projects);
            return "project-m";
        }
        else{
            projects = user.getProjects_d();
            model.addAttribute("text", "Проекты в которых вы учавствуете:");
            model.addAttribute("project", projects);
            return "project-d";
        }
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute User userForm,@RequestParam(name = "isRole") Long role, Model model) {
        if(userForm.getFname().equals("") || userForm.getLname().equals("") || userForm.getUsername().equals("")
                || userForm.getPassword().equals("")){
            model.addAttribute("registererror","Не корректно введены данные, повторите ввод!");
            model.addAttribute("userForm", new User());
            return "registration";
        }
        if(userService.findByUsername(userForm.getUsername()) != null){
            model.addAttribute("registererror","Пользователь с таким email уже существует!");
            model.addAttribute("userForm", new User());
            return "registration";
        }
        try {
            registrationService.save(userForm, role);
        }
        catch (MailSendException e){
            model.addAttribute("registererror", "Введите корректный Email");
            model.addAttribute("userForm", new User());
            return "registration";
        }
        catch (MailException e)
        {
            model.addAttribute("registererror", "Не получилось отправить письмо на почту");
            model.addAttribute("userForm", new User());
            return "registration";
        }
        model.addAttribute("loginerror", "Письмо с подтверждением было отправлено вам на почту");
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginerror", "");
        return "login";
    }

    @GetMapping("/")
    public String login1(Model model, HttpServletRequest request, HttpServletResponse response){
        User user = userService.findByUsername(securityService.getCurrentUsername());
        if(user.getRoles().getName().equals("ROLE_ANONIM")){
            logout(request,response);
            model.addAttribute("text", "Ваш аккаунт не подтверждён");
            return "error";
        }
        return "index";

    }

    @GetMapping("/activation")
    public String activation(@RequestParam(name = "name", required = false, defaultValue = "") String name,                        @RequestParam(name = "code", required = false, defaultValue = "") String code, Model model){
        if(name.equals("") || code.equals("")){
            model.addAttribute("text", "Ошибка активации");
            return "error";
        }
        try{
            Anonim anonim = registrationService.findByName(name);
            if(anonim.getCode().equals(code)) {
                User user = userService.findByUsername(name);
                user.setRoles(roleService.findById(anonim.getRole()));
                userService.saveeasy(user);
                registrationService.deleteById(anonim.getId());
            }
            else {
                model.addAttribute("text", "Ошибка активации, не верный код");
                return "error";
            }

        }
        catch (NullPointerException e){
            model.addAttribute("text", "Произошла ошибка");
            return "error";
        }
        model.addAttribute("loginerror", "Активация прошла успешно, можете авторизироваться!");
        return "redirect:/login";
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}