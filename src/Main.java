import enums.TaskType;
import service.TaskHandler;
import service.handler.impl.*;


void main(String[] args) {

    TaskType taskType = TaskType.convert(args[0].strip().toLowerCase());

    AddTask addTask = new AddTask();
    UpdateTask updateTask = new UpdateTask();
    DeleteTask deleteTask = new DeleteTask();
    MarkInProgress markInProgress = new MarkInProgress();
    MarkDone markDone = new MarkDone();
    ViewTask viewTask = new ViewTask();

    List<TaskHandler> taskHandlers = new ArrayList<>();
    taskHandlers.add(addTask);
    taskHandlers.add(updateTask);
    taskHandlers.add(deleteTask);
    taskHandlers.add(markInProgress);
    taskHandlers.add(markDone);
    taskHandlers.add(viewTask);

    taskHandlers.stream()
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
