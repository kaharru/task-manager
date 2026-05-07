package asylbek.taskmanager.controller;

import asylbek.taskmanager.dto.TaskCreateDTO;
import asylbek.taskmanager.entity.Task;
import asylbek.taskmanager.entity.TaskStatus;
import asylbek.taskmanager.entity.Priority;
import asylbek.taskmanager.entity.User;
import asylbek.taskmanager.repository.UserRepository;
import asylbek.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    // GET информация о текущем пользователе
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не авторизован");
        }

        User currentUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Map<String, String> response = new HashMap<>();
        response.put("email", currentUser.getEmail());
        response.put("name", currentUser.getName());

        return ResponseEntity.ok(response);
    }

    // GET все задачи текущего пользователя
    @GetMapping
    public List<Task> getAllTasks(Authentication auth) {
        System.out.println("=== GET ВСЕ ЗАДАЧИ ===");
        System.out.println("Пользователь: " + (auth != null ? auth.getName() : "null"));

        if (auth == null) {
            return List.of();
        }

        User currentUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return taskService.getTasksByUser(currentUser.getId());
    }

    // GET задача по ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, Authentication auth) {
        System.out.println("=== GET ЗАДАЧА ПО ID ===");

        Task task = taskService.getTaskById(id).orElse(null);
        if (task == null || task.getUser() == null || !task.getUser().getEmail().equals(auth.getName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    // GET задачи пользователя
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId);
    }

    // GET поиск по названию
    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String q) {
        return taskService.searchTasks(q);
    }

    // POST создать задачу
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskCreateDTO dto, Authentication auth) {
        try {
            System.out.println("=== СОЗДАНИЕ ЗАДАЧИ ===");
            System.out.println("Пользователь: " + (auth != null ? auth.getName() : "null"));

            if (auth == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Не авторизован");
            }

            User currentUser = userRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            Task task = new Task();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(TaskStatus.NEW);
            task.setUser(currentUser);  // ← Устанавливаем пользователя

            try {
                task.setPriority(Priority.valueOf(dto.getPriority()));
            } catch (IllegalArgumentException e) {
                task.setPriority(Priority.MEDIUM);
            }

            Task saved = taskService.createTask(task);  // ← Только один аргумент!

            return new ResponseEntity<>(saved, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка: " + e.getMessage());
        }
    }

    // PUT обновить задачу
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task, Authentication auth) {
        try {
            System.out.println("=== ОБНОВЛЕНИЕ ЗАДАЧИ ===");
            System.out.println("ID: " + id);
            System.out.println("Пользователь: " + (auth != null ? auth.getName() : "null"));

            Task existing = taskService.getTaskById(id).orElse(null);
            if (existing == null || existing.getUser() == null || !existing.getUser().getEmail().equals(auth.getName())) {
                System.out.println("Задача не найдена или не принадлежит пользователю");
                return ResponseEntity.notFound().build();
            }

            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setStatus(task.getStatus());
            existing.setPriority(task.getPriority());

            Task updated = taskService.updateTask(id, existing);
            System.out.println("✅ Задача обновлена");
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            System.err.println("❌ ОШИБКА ОБНОВЛЕНИЯ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обновления: " + e.getMessage());
        }
    }

    // DELETE удалить задачу
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication auth) {
        try {
            System.out.println("=== УДАЛЕНИЕ ЗАДАЧИ ===");
            System.out.println("ID: " + id);

            Task existing = taskService.getTaskById(id).orElse(null);
            if (existing == null || existing.getUser() == null || !existing.getUser().getEmail().equals(auth.getName())) {
                System.out.println("Задача не найдена или не принадлежит пользователю");
                return ResponseEntity.notFound().build();
            }

            taskService.deleteTask(id);
            System.out.println("✅ Задача удалена");
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            System.err.println("❌ ОШИБКА УДАЛЕНИЯ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка удаления: " + e.getMessage());
        }
    }
}