package service;

import enums.TaskType;
import model.TaskRequest;

public interface TaskHandler {

    boolean handle(TaskType taskType);

    TaskRequest resolve(String args[]);

    void execute(TaskRequest taskRequest) throws Exception;
}
