package hello.controller;

import hello.model.Comment;
import hello.model.Project;
import hello.model.Task;
import hello.model.User;
import hello.service.CommentService;
import hello.service.SecurityService;
import hello.service.TaskService;
import hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public String taskInfo(@PathVariable("id") Long id, Model model){
        try {
            Task task = taskService.findById(id);
            model.addAttribute("id", task.getId());
            User actual_user = userService.findByUsername(securityService.getCurrentUsername());
            Project project = task.getProject();
            if(!(project.getDevelopers().contains(actual_user) || project.getManager().equals(actual_user))){
                model.addAttribute("text", "Вам отказано в доступе");
                return "error";
            }
           // User author = task.getAuthor();
            try {
                User developer = task.getDeveloper();
                model.addAttribute("developer", developer.getUsername());
            }
            catch (NullPointerException e){
                model.addAttribute("developer", "отсутствует");
            }
            Set<Comment> comments = task.getComments();
            model.addAttribute("comments", comments);
            model.addAttribute("task", task);
            if(actual_user.getRoles().getName().equals("ROLE_MANAGER")) return "task_infom";
            return "task_infod";
        }
        catch (EntityNotFoundException e){
            model.addAttribute("text", "Такого таска не существует");
        }
        return "error";
    }

    @GetMapping("/{id}/change/{comment}")
    public String changeCom(@PathVariable("id") Long id,@PathVariable("comment") Long cId, Model model){
        Task task = taskService.findById(id);
        Project project = task.getProject();
        User actual_user = userService.findByUsername(securityService.getCurrentUsername());
        if(!(project.getDevelopers().contains(actual_user) || project.getManager().equals(actual_user))){
            model.addAttribute("text", "Вам отказано в доступе");
            return "error";
        }
        Comment comment = commentService.findById(cId);
        model.addAttribute("id", id);
        model.addAttribute("cId", cId);
        model.addAttribute("comment", comment);
        return "change-comment";
    }
    @RequestMapping(value = "/{id}/change/{comment}/submit", method = RequestMethod.POST)
    public String change(@ModelAttribute Comment comment, @PathVariable("id") Long id,@PathVariable("comment") Long cId) {
        Comment changeCom = commentService.findById(cId);
        changeCom.setId(cId);
        changeCom.setText(comment.getText());
        commentService.save(changeCom);
        return "redirect:/task/" + id;
    }
    @GetMapping(value = "/{id}/change/{comment}/delete")
    public String delete(@PathVariable("id") Long id,@PathVariable("comment") Long cId, Model model) {
        Task task = taskService.findById(id);
        Project project = task.getProject();
        User actual_user = userService.findByUsername(securityService.getCurrentUsername());
        if(!(project.getDevelopers().contains(actual_user) || project.getManager().equals(actual_user))){
            model.addAttribute("text", "Вам отказано в доступе");
            return "error";
        }
        commentService.deleteById(cId);
        return "redirect:/task/" + id;
    }
    @RequestMapping(value = "/{id}/create-comment", method = RequestMethod.POST)
    public String createComm(@RequestParam(name="commentText", required=false, defaultValue="пустой комментарий") String comText, @PathVariable("id") Long id, Model model) {
        User actual_user = userService.findByUsername(securityService.getCurrentUsername());
        Task task = taskService.findById(id);
        Project project = task.getProject();
        if(!(project.getDevelopers().contains(actual_user) || project.getManager().equals(actual_user))){
            model.addAttribute("text", "Вам отказано в доступе");
            return "error";
        }
        Comment comment = new Comment();
        comment.setText(comText);
        comment.setAuthor(actual_user);
        comment.setTask(taskService.findById(id));
        commentService.save(comment);
        return "redirect:/task/" + id;
    }
    @GetMapping(value = "/{id}/status/{sid}")
    public String status(@PathVariable("id") Long id,@PathVariable("sid") Integer sId, Model model) {
        Task task = taskService.findById(id);
        Project project = task.getProject();
        User actual_user = userService.findByUsername(securityService.getCurrentUsername());
        if(!(project.getDevelopers().contains(actual_user) || project.getManager().equals(actual_user))){
            model.addAttribute("text", "Вам отказано в доступе");
            return "error";
        }
        switch (sId){
            case 1: task.setStatus("Waiting"); break;
            case 2: task.setStatus("Implementation"); break;
            case 3: task.setStatus("Verifying"); break;
            case 4: task.setStatus("Releasing"); break;
            default: break;
        }
        taskService.save(task);
        model.addAttribute("task", task);
        return "redirect:/task/" + id;
    }

    @GetMapping(value = "/{id}/developer/")
    public String developer(@PathVariable("id") Long id, Model model) {

        Task task = taskService.findById(id);
        Project project = task.getProject();
        User actual_user = userService.findByUsername(securityService.getCurrentUsername());
        if(!(project.getManager().equals(actual_user))){
            model.addAttribute("text", "Вам отказано в доступе");
            return "error";
        }
        Set<User> developers = project.getDevelopers();
        model.addAttribute("id", id);
        model.addAttribute("developer", developers);
        return "set-developer";
    }

    @GetMapping(value = "/{id}/developer/{did}/add")
    public String developer(@PathVariable("id") Long id, @PathVariable(name = "did") Long dId, Model model) {
        Task task = taskService.findById(id);
        Project project = task.getProject();
        User actual_user = userService.findByUsername(securityService.getCurrentUsername());
        if(!(project.getManager().equals(actual_user))){
            model.addAttribute("text", "Вам отказано в доступе");
            return "error";
        }
        task.setDeveloper(userService.findById(dId));
        taskService.save(task);
        Long idd = task.getId();
        return "redirect:/task/" + idd;
    }
}
