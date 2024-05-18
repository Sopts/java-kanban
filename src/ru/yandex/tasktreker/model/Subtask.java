package ru.yandex.tasktreker.model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.setTaskType(TaskType.SUBTASK);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

}