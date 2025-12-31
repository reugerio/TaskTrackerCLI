package service.handler.impl;

import enums.TaskStatus;
import enums.TaskType;
import model.Task;
import model.TaskRequest;
import service.handler.BaseHandler;

import java.time.OffsetDateTime;
import java.util.List;

public class ViewTask extends BaseHandler {

    @Override
    public boolean handle(TaskType taskType) {
        return taskType.equals(TaskType.LIST);
    }

    @Override
    public TaskRequest resolve(String[] args) {

        TaskRequest taskRequest = new TaskRequest();

        if (args.length > 1) {
            validateArgsLength(args, 2);
            taskRequest.setStatus(args[1]);
        }

        return taskRequest;
    }

    @Override
    public void execute(TaskRequest taskRequest) throws Exception {
        List<Task> tasks = getTasks();

        if (taskRequest.getStatus() == null) {
            printTasks(tasks);
            return;
        }

        tasks = tasks.stream()
                .filter(x -> x.getStatus().equals(taskRequest.getStatus()))
                .toList();

        printTasks(tasks);
    }

    private void printTasks(List<Task> tasks) {
        System.out.println("+----+--------------+--------+---------------------------------+-----------------------------------+");
        System.out.println("| ID | Description  | Status | Created At                      | Updated At                        |");
        System.out.println("+----+--------------+--------+---------------------------------+-----------------------------------+");

        tasks.forEach(x -> System.out.printf("| %-2s | %-12s | %-6s | %-29s | %-29s |%n"
                .formatted(
                        x.getId(),
                        x.getDescription(),
                        x.getStatus(),
                        x.getCreatedAt().toString(),
                        x.getUpdatedAt().toString()
                )));

        System.out.println("+----+--------------+--------+---------------------------------+-----------------------------------+");
    }
}
