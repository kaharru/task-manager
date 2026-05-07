# Task Manager

## О проекте
Веб-приложение для управления задачами с аутентификацией пользователей.

## Функционал
- Регистрация и вход пользователей
- Создание, просмотр, редактирование, удаление задач
- Изменение статуса задачи (NEW, IN_PROGRESS, DONE)
- Приоритет задач (LOW, MEDIUM, HIGH)
- Поиск задач по названию
- Красивый современный UI

## Технологии
- Java 17, Spring Boot, Spring Security
- PostgreSQL, Hibernate
- Thymeleaf, HTML5/CSS3/JavaScript
- Maven, Git

## Запуск
1. Установить PostgreSQL и создать БД `taskmanager`
2. Настроить `application.properties` (пароль от БД)
3. Запустить `TaskManagerApplication.java`
4. Открыть `http://localhost:8080/register`

## Авторы
- **[Асылбек]** — бэкенд, фронтенд, архитектура
- **[Арман]** — сущность User, аутентификация, документация
