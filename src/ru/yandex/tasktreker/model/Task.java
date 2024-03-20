package ru.yandex.tasktreker.model;

public class Task {
    private int id;
    private final String name;
    private final String description;
    private Status status;

    public Task (String nameTask, String description) {
        this.name = nameTask;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
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

}