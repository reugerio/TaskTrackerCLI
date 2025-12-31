package service.handler.impl;

import enums.TaskStatus;
import enums.TaskType;
import model.Task;
import model.TaskRequest;
import service.handler.BaseTask;

import java.time.OffsetDateTime;
import java.util.List;

public class AddTask extends BaseTask {

    @Override
    public boolean handle(TaskType taskType) {
        return taskType.equals(TaskType.ADD);
    }

    @Override
    public TaskRequest resolve(String args[]) {

        validateArgsLength(args, 2);

        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setDescription(args[1]);

        return taskRequest;
    }

    @Override
    public void execute(TaskRequest taskRequest) throws Exception {
        List<Task> tasks = getTasks();

        int newId = tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;

        Task newTask = new Task();
        newTask.setId(newId);
        newTask.setDescription(taskRequest.getDescription());
        newTask.setStatus(TaskStatus.TODO.getValue());
        newTask.setCreatedAt(OffsetDateTime.now());
        newTask.setUpdatedAt(OffsetDateTime.now());

        tasks.add(newTask);

        saveTasks(tasks);

        System.out.println("Added new task with ID: " + newId);
    }
}
