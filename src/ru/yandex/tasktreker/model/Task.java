package ru.yandex.tasktreker.model;

import java.util.Objects;

public class Task {
    private int id;
    private final String name;
    private final String description;
    private Status status;
    private TaskType taskType;

    public Task(String nameTask, String description) {
        this.name = nameTask;
        this.description = description;
        this.status = Status.NEW;
        this.taskType = TaskType.TASK;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public String getName() {

        return name;
    }

    public String getDescription() {

        return description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}