package asylbek.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
        System.out.println("🚀 Task Manager запущен!");
        System.out.println("📋 API: http://localhost:8080/api/tasks");
        System.out.println("🌐 Интерфейс: http://localhost:8080/tasks");
    }
}