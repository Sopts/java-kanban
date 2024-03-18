package ru.yandex.tasktreker.model;

public class Subtask extends Task {

    private int epicId;

    private final Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public int getEpicId() {

        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Epic getEpic() {

        return epic;
    }
}


