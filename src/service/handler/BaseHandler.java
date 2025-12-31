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
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            sb.append('{');

            // id
            sb.append("\"id\":");
            appendJsonString(sb, String.valueOf(t.getId()));
            sb.append(',');

            // description
            sb.append("\"description\":");
            appendJsonString(sb, t.getDescription());
            sb.append(',');

            // status (use toString() which was implemented in the enum)
            sb.append("\"status\":");
            appendJsonString(sb, t.getStatus() == null ? null : t.getStatus().toString());
            sb.append(',');

            // createdAt
            sb.append("\"createdAt\":");
            appendJsonString(sb, t.getCreatedAt() == null ? null : t.getCreatedAt().toString());
            sb.append(',');

            // updatedAt
            sb.append("\"updatedAt\":");
            appendJsonString(sb, t.getUpdatedAt() == null ? null : t.getUpdatedAt().toString());

            sb.append('}');
            if (i < tasks.size() - 1) {
                sb.append(',');
            }
        }

        sb.append(']');

        try {
            // ensure parent directory exists
            Path parent = JSON_FILE_PATH.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(JSON_FILE_PATH, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to " + JSON_FILE_PATH, e);
        }
    }

    private static void appendJsonString(StringBuilder sb, String value) {
        if (value == null) {
            sb.append("null");
            return;
        }
        sb.append('"');
        sb.append(escapeJson(value));
        sb.append('"');
    }

    private static String escapeJson(String s) {
        StringBuilder out = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    out.append("\\\"");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                case '\b':
                    out.append("\\b");
                    break;
                case '\f':
                    out.append("\\f");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }
        return out.toString();
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
