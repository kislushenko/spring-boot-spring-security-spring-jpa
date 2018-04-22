package hello.controller;

import hello.model.Project;
import hello.model.Task;
import hello.model.User;
import hello.service.ProjectService;
import hello.service.SecurityService;
import hello.service.TaskService;
import hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, @RequestParam(name = "sort", required = false, defaultValue = "0") int sort, Model model) {

        try {
            Project project = projectService.findById(id);


            Set<Task> tasks = project.getTasks();
            User user = userService.findByUsername(securityService.getCurrentUsername());
            if (user.equals(project.getManager()) || project.getDevelopers().contains(user)) {
                model.addAttribute("id", project.getId());
                if (sort == 0 || user.getRoles().getName().equals("ROLE_MANAGER")) {
                    model.addAttribute("task", tasks);
                    if (user.getRoles().getName().equals("ROLE_DEVELOPER")) return "project_editd";
                    return "project_editm";

                } else {
                    Set<Task> taskDeveloper = new HashSet<>();
                    for (Task task : tasks) {
                        try {
                            if (task.getDeveloper().equals(user)) {
                                taskDeveloper.add(task);
                            }
                        } catch (NullPointerException e) {
                            System.out.println(task.getName() + " без девелопера");
                        }
                    }
                    model.addAttribute("task", taskDeveloper);
                    return "project_editd";
                }

            } else {
                model.addAttribute("text", "Вам отказано в доступе к данному проекту!\n Возможно вы не являетесь его разработчиком.");
            }
            return "error";
        } catch (EntityNotFoundException e) {
            model.addAttribute("text", "Такого проекта не существует");
            return "error";
        }

    }

    @RequestMapping(value = "/{id}/add-developer", method = RequestMethod.GET)
    public String addDeveloper(@PathVariable("id") Long id, Model model) {
        Project project = projectService.findById(id);
        User user = userService.findByUsername(securityService.getCurrentUsername());
        if (!(user.equals(project.getManager()))) {
            model.addAttribute("text","Вам отказано в доступе");
            return "error";
        }
        model.addAttribute("id", id);
        model.addAttribute("findnull"," ");
        return "add-developer";
    }

    @RequestMapping(value = "/{id}/add-developer", method = RequestMethod.POST)
    public String addDeveloper2(@RequestParam(name="fname") String name,
                                @RequestParam(name="lname") String lname,
                                @PathVariable("id") Long id, Model model){
        if(name == "" || lname == "") model.addAttribute("findnull", "Введите корректные данные");
        Set<User> developers = userService.findAllByFNameAndLName(name,lname);
        Set<User> developersUser = new HashSet<>();
        for(User develop: developers){
            if(develop.getRoles().getName().equals("ROLE_DEVELOPER")) developersUser.add(develop);
        }
        if(developersUser.isEmpty()){
            model.addAttribute("findnull", "Такого разработчика не существует!");
            return "add-developer";
        }
        model.addAttribute("developers", developersUser);
        return "add-developer";
    }

    @GetMapping(value = "/{id}/add/{did}")
    public String addDeveloper(@PathVariable(name = "id") Long id, @PathVariable(name = "did") Long dId, Model model)
    {
        Project project = projectService.findById(id);
        User user = userService.findByUsername(securityService.getCurrentUsername());
        User developer = userService.findById(dId);
        if (!(user.equals(project.getManager()))) {
            model.addAttribute("text","Вам отказано в доступе");
            return "error";
        }
        if(developer.getProjects_d().contains(project))
        {
            model.addAttribute("findnull", "Данный разработчик уже добавлен в проект");
            return "add-developer";
        }

        project.addDevelopers(developer);
        projectService.save(project);
        model.addAttribute("findnull", "Разработчик успешно добавлен в проект");
        return "add-developer";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newProject(Model model) {
        model.addAttribute("createnull", " ");
        return "new-project";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newProject1(@RequestParam(name = "namepr") String name, Model model) {
        if(name == "") {
            model.addAttribute("createnull", "Имя введено не верно");
            return "new-project";
        }
        Project project = new Project();
        project.setName(name);
        User manager = userService.findByUsername(securityService.getCurrentUsername());
        project.setManager(manager);
        projectService.save(project);
        return "redirect:/project";
    }

    @RequestMapping(value = "/{id}/add-task", method = RequestMethod.GET)
    public String addTask(@PathVariable("id") Long id, Model model) {
        Project project = projectService.findById(id);
        User user = userService.findByUsername(securityService.getCurrentUsername());
        if (!(user.equals(project.getManager()) || project.getDevelopers().contains(user))) {
            model.addAttribute("text","Вам отказано в доступе");
            return "error";
        }
        model.addAttribute("id", id);
        return "new-task";
    }

    @RequestMapping(value = "/{id}/add-task", method = RequestMethod.POST)
    public String addTask2(@RequestParam(name="taskn") String name,
                                @RequestParam(name="text") String text,
                                @PathVariable("id") Long id, Model model){
        if(name == "" || text == ""){
            model.addAttribute("createnull", "Не введены все данные");
            return "new-task";
        }
        Task task = new Task();
        task.setName(name);
        task.setText(text);
        User manager = userService.findByUsername(securityService.getCurrentUsername());
        task.setAuthor(manager);
        task.setStatus("Waiting");
        task.setProject(projectService.findById(id));
        taskService.save(task);
        return "redirect:/project/" + id;
    }
}
