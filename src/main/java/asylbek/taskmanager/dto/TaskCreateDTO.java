package asylbek.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskCreateDTO {

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 3, max = 100, message = "Название от 3 до 100 символов")
    private String title;

    @Size(max = 500, message = "Описание не длиннее 500 символов")
    private String description;

    private String priority = "MEDIUM";

    // ПУСТОЙ КОНСТРУКТОР (ОБЯЗАТЕЛЬНО ДЛЯ JSON)
    public TaskCreateDTO() {}

    // Геттеры и сеттеры
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}