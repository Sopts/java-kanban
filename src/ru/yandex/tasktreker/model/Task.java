package ru.yandex.tasktreker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private int id;
    private final String name;
    private final String description;
    private Status status;
    private TaskType taskType;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String nameTask, String description, LocalDateTime startTime, Duration duration) {
        this.name = nameTask;
        this.description = description;
        this.status = Status.NEW;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration.toMinutes());
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }


    @Override
    public int compareTo(Task other) {
        return this.getStartTime().compareTo(other.getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status && taskType == task.taskType && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, taskType, duration, startTime);
    }

    public String getStartTimeString() {
        return startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + getStartTimeString() +
                '}';
    }

}