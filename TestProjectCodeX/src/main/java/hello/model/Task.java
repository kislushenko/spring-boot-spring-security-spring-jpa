package hello.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinTable(name = "pr_task", joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Project project;

    @ManyToOne
    @JoinTable(name = "developer_task", joinColumns = @JoinColumn(name = "task_id"),
                inverseJoinColumns = @JoinColumn(name = "developer_id"))
    private User developer;

    @ManyToOne
    @JoinTable(name = "task_author", joinColumns = @JoinColumn(name = "task_id"),
                inverseJoinColumns = @JoinColumn(name = "author_id"))
    private User author;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "task")
    private Set<Comment> comments;

    public Task() {
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getDeveloper() {
        return developer;
    }

    public void setDeveloper(User developer) {
        this.developer = developer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


}
