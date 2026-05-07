package asylbek.taskmanager.service;

import asylbek.taskmanager.entity.Task;
import asylbek.taskmanager.entity.User;
import asylbek.taskmanager.repository.TaskRepository;
import asylbek.taskmanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTasks() {
        log.info("📋 Запрос всех задач");
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        log.info("🔍 Поиск задачи с ID: {}", id);
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByUser(Long userId) {
        log.info("👤 Запрос задач пользователя ID: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        return user.map(taskRepository::findByUser).orElse(List.of());
    }

    public List<Task> searchTasks(String keyword) {
        log.info("🔎 Поиск задач по ключевому слову: {}", keyword);
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }

    @Transactional
    public Task createTask(Task task) {
        log.info("📝 Создание задачи: {}", task.getTitle());
        Task saved = taskRepository.save(task);
        log.info("✅ Задача создана с ID: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        log.info("✏️ Обновление задачи с ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена с ID: " + id));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());

        if (taskDetails.getUser() != null) {
            task.setUser(taskDetails.getUser());
        }

        Task updated = taskRepository.save(task);
        log.info("✅ Задача {} обновлена", id);
        return updated;
    }

    @Transactional
    public void deleteTask(Long id) {
        log.warn("🗑 Удаление задачи с ID: {}", id);
        taskRepository.deleteById(id);
        log.info("✅ Задача {} удалена", id);
    }
}