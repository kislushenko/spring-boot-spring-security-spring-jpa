package hello.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fname")
    private String fname;

    @Column(name = "lname")
    private String lname;

    @ManyToOne()
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role roles;

    @OneToMany(mappedBy = "manager")
    private Set<Project> projects_m;

    @ManyToMany(mappedBy = "developers")
    private Set<Project> projects_d;

    @OneToMany(mappedBy = "developer")
    private Set<Task> tasks;

    @OneToMany(mappedBy = "author")
    private Set<Task> task_author;

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;

    public User() {
    }

    public Set<Task> getTask_author() {
        return task_author;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setTask_author(Set<Task> task_author) {
        this.task_author = task_author;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Project> getProjects_m() {
        return projects_m;
    }

    public void setProjects_m(Set<Project> projects_m) {
        this.projects_m = projects_m;
    }

    public Set<Project> getProjects_d() {
        return projects_d;
    }

    public void setProjects_d(Set<Project> projects_d) {
        this.projects_d = projects_d;
    }

    public void addProject_d(Project project) {
        projects_d.add(project);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" +
                '}';
    }
}