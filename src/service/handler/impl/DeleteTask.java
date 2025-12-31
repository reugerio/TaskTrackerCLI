package service.handler.impl;

import enums.TaskType;
import model.Task;
import model.TaskRequest;
import service.handler.BaseTask;

import java.util.List;

public class DeleteTask extends BaseTask {

    @Override
    public boolean handle(TaskType taskType) {
        return taskType.equals(TaskType.DELETE);
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

        tasks = tasks.stream()
                .filter(task -> task.getId() != taskRequest.getId())
                .toList();

        saveTasks(tasks);
    }
}
