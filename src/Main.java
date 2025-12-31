import enums.TaskType;
import service.TaskManager;
import service.handler.impl.*;


void main(String[] args) {

    TaskType taskType = TaskType.convert(args[0].strip().toLowerCase());

    AddTask addTask = new AddTask();
    UpdateTask updateTask = new UpdateTask();
    DeleteTask deleteTask = new DeleteTask();
    MarkInProgress markInProgress = new MarkInProgress();
    MarkDone markDone = new MarkDone();
    ViewTask viewTask = new ViewTask();

    List<TaskManager> taskManager = new ArrayList<>();
    taskManager.add(addTask);
    taskManager.add(updateTask);
    taskManager.add(deleteTask);
    taskManager.add(markInProgress);
    taskManager.add(markDone);
    taskManager.add(viewTask);

    taskManager.stream()
            .filter(handler -> handler.handle(taskType))
            .findFirst()
            .ifPresentOrElse(handler -> {
                try {
                    handler.execute(handler.resolve(args));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, () -> {
                throw new RuntimeException("No handler found for task type: " + taskType);
            });
}
