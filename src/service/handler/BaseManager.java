package service.handler;

import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;

import static util.JsonHelper.*;

public abstract class BaseManager implements TaskManager {

    private static final Path JSON_FILE_PATH = Path.of("src/tasks.json");

    protected void validateArgsLength(String[] args, int expectedLength) {
        if (args.length != expectedLength) {
            throw new IllegalArgumentException("Expected " + expectedLength + " arguments, but got " + args.length);
        }
    }

    protected List<Task> getTasks() throws IOException {

        List<Task> tasks = new ArrayList<>();

        String jsonContent = Files.readString(JSON_FILE_PATH);

        if (jsonContent.isEmpty()) {
            return tasks;
        }

        String[] jsonTasks = jsonContent
                .replace("[", "")
                .replace("]", "")
                .split("},");

        Arrays.stream(jsonTasks).forEach(jsonTask -> {
            Map<String, String> taskMap = parseJsonObject(jsonTask);

            String id = taskMap.get("id");
            String description = taskMap.get("description");
            String status = taskMap.get("status");
            OffsetDateTime createdDateTime = convertToDateTime(taskMap.get("createdAt"));
            OffsetDateTime updatedDateTime = convertToDateTime(taskMap.get("updatedAt"));

            Task task = new Task();
            task.setId(Integer.parseInt(id));
            task.setDescription(description);
            task.setStatus(status);
            task.setCreatedAt(createdDateTime);
            task.setUpdatedAt(updatedDateTime);

            tasks.add(task);
        });

        return tasks;
    }

    protected void saveTasks(List<Task> tasks) {

        String jsonString = buildJsonString(tasks);

        try {
            Files.writeString(JSON_FILE_PATH, jsonString, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to " + JSON_FILE_PATH, e);
        }
    }

    private OffsetDateTime convertToDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.equals("null") || dateTimeStr.isEmpty()) {
            return null;
        }

        dateTimeStr = dateTimeStr.replaceAll("\\s+", "");

        return OffsetDateTime.parse(dateTimeStr);
    }
}
