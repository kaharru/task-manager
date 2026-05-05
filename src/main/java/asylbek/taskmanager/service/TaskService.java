package asylbek.taskmanager.service;

import asylbek.taskmanager.entity.Task;
import asylbek.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    // Получить все задачи
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Получить задачу по ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Получить задачи пользователя
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    // Создать задачу
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Обновить задачу
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена с id: " + id));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setUserId(taskDetails.getUserId());

        return taskRepository.save(task);
    }

    // Удалить задачу
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
