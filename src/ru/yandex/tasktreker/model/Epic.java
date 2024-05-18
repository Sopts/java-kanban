package ru.yandex.tasktreker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.setTaskType(TaskType.EPIC);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteSubtasksList() {
        subtasks.clear();
    }

}