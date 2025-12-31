package service.handler;

import model.Task;
import service.TaskHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.JsonHelper.appendJsonString;
import static util.JsonHelper.buildJsonString;

public abstract class BaseHandler implements TaskHandler {

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
            jsonTask = jsonTask.replace("\"", "");
            String[] taskProperties = jsonTask.replace("{", "").replace("}", "").split(",");

            String id = getStringValue(taskProperties, 0);
            String description = getStringValue(taskProperties, 1);
            String status = getStringValue(taskProperties, 2);
            OffsetDateTime createdDateTime = convertToDateTime(getDateValue(taskProperties, 3));
            OffsetDateTime updatedDateTime = convertToDateTime(getDateValue(taskProperties, 4));

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

    private String getStringValue(String[] keyValue, int index) {

        if (index >= keyValue.length) {
            return null;
        }

        return keyValue[index].split(":")[1].strip();
    }

    private String getDateValue(String[] keyValue, int index) {

        if (index >= keyValue.length) {
            return null;
        }

        return keyValue[index].split("[a-z]:")[1];
    }

    private OffsetDateTime convertToDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.equals("null") || dateTimeStr.isEmpty()) {
            return null;
        }

        dateTimeStr = dateTimeStr.replaceAll("\\s+", "");

        return OffsetDateTime.parse(dateTimeStr);
    }
}
