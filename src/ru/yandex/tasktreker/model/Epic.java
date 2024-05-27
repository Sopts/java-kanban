package ru.yandex.tasktreker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private final List<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;


    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.setTaskType(TaskType.EPIC);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);

        Duration totalDuration = Duration.ZERO;
        for (Subtask task : subtasks) {
            totalDuration = totalDuration.plus(task.getDuration());
        }
        setDuration(totalDuration);

        LocalDateTime earliestStartTime = null;
        for (Subtask task : subtasks) {
            if (earliestStartTime == null || task.getStartTime().isBefore(earliestStartTime)) {
                earliestStartTime = task.getStartTime();
            }
        }
        setStartTime(earliestStartTime);

        LocalDateTime latestEndTime = null;
        for (Subtask task : subtasks) {
            if (latestEndTime == null || task.getEndTime().isAfter(latestEndTime)) {
                latestEndTime = task.getEndTime();
            }
        }
        setEndTime(latestEndTime);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);

        Duration totalDuration = Duration.ZERO;
        for (Subtask task : subtasks) {
            totalDuration = totalDuration.plus(task.getDuration());
        }
        setDuration(totalDuration);

        LocalDateTime earliestStartTime = null;
        for (Subtask task : subtasks) {
            if (earliestStartTime == null || task.getStartTime().isBefore(earliestStartTime)) {
                earliestStartTime = task.getStartTime();
            }
        }
        setStartTime(earliestStartTime);

        LocalDateTime latestEndTime = null;
        for (Subtask task : subtasks) {
            if (latestEndTime == null || task.getEndTime().isAfter(latestEndTime)) {
                latestEndTime = task.getEndTime();
            }
        }
        setEndTime(latestEndTime);
    }


    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteSubtasksList() {
        subtasks.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}