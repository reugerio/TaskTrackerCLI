package service.handler.impl;

import enums.TaskType;
import model.Task;
import model.TaskRequest;
import service.handler.BaseManager;

import java.time.OffsetDateTime;
import java.util.List;

public class UpdateTask extends BaseManager {

    @Override
    public boolean handle(TaskType taskType) {
        return taskType.equals(TaskType.UPDATE);
    }

    @Override
    public TaskRequest resolve(String[] args) {

        validateArgsLength(args, 3);

        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setId(Integer.parseInt(args[1]));
        taskRequest.setDescription(args[2]);

        return taskRequest;
    }

    @Override
    public void execute(TaskRequest taskRequest) throws Exception {
        List<Task> tasks = getTasks();

        tasks.stream()
                .filter(task -> task.getId() == taskRequest.getId())
                .findFirst()
                .ifPresentOrElse(task -> {
                    task.setDescription(taskRequest.getDescription());
                    task.setUpdatedAt(OffsetDateTime.now());
                }, () -> {
                    System.out.println("No task id " + taskRequest.getId());
                });

        saveTasks(tasks);
    }
}
