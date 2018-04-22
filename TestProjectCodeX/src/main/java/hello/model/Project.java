package hello.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinTable(name = "pr_manager", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "manager_id"))
    private User manager;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;

    @ManyToMany
    @JoinTable(name = "pr_developer", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id"))
    private Set<User> developers;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void addDevelopers(User user){ developers.add(user); }

    public Set<User> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<User> developers) {
        this.developers = developers;
    }

    public void addTask(Task task){ tasks.add(task); }
}
