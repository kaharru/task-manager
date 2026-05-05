package asylbek.taskmanager.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;   // NEW, IN_PROGRESS, DONE
    private String priority; // LOW, MEDIUM, HIGH

    @Column(name = "user_id")
    private Long userId;

    // Пустой конструктор (нужен для JPA)
    public Task() {}

    // Конструктор с полями
    public Task(String title, String description, String status, String priority, Long userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.userId = userId;
    }

    // Геттеры (чтобы получать значения)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public Long getUserId() { return userId; }

    // Сеттеры (чтобы изменять значения)
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setUserId(Long userId) { this.userId = userId; }
}