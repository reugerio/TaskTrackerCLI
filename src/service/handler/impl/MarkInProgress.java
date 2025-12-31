package service.handler.impl;

import enums.TaskStatus;
import enums.TaskType;
import model.Task;
import model.TaskRequest;
import service.handler.BaseManager;

import java.time.OffsetDateTime;
import java.util.List;

public class MarkInProgress extends BaseManager {

    @Override
    public boolean handle(TaskType taskType) {
        return taskType.equals(TaskType.MARK_IN_PROGRESS);
    }

    @Override
    public TaskRequest resolve(String[] args) {

        validateArgsLength(args, 2);

        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setId(Integer.parseInt(args[1]));

        return taskRequest;
    }

    @Override
    public void execute(TaskRequest taskRequest) throws Exception {
        List<Task> tasks = getTasks();

        tasks.stream()
                .filter(task -> task.getId() == taskRequest.getId())
                .findFirst()
                .ifPresentOrElse(task -> {
                    task.setStatus(TaskStatus.IN_PROGRESS.getValue());
                    task.setUpdatedAt(OffsetDateTime.now());
                }, () -> {
                    System.out.println("No task id " + taskRequest.getId());
                });

        saveTasks(tasks);
    }
}
