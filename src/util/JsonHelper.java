package util;

import model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new HashMap<>();

        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].replace("\"", "").trim();
                String value = kv[1].replace("\"", "").trim();
                map.put(key, value);
            }
        }
        return map;
    }

    public static String buildJsonString(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder("[");
        sb.append('\n');

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            // Open object
            sb.append('{');

            // id
            sb.append("\"id\":").append(t.getId()).append(',');

            // description
            sb.append("\"description\":");
            appendJsonString(sb, t.getDescription());

            // status
            sb.append(',').append("\"status\":");
            appendJsonString(sb, t.getStatus() == null ? null : t.getStatus());

            // createdAt
            sb.append(',').append("\"createdAt\":");
            appendJsonString(sb, t.getCreatedAt() == null ? null : t.getCreatedAt().toString());

            // updatedAt
            sb.append(',').append("\"updatedAt\":");
            appendJsonString(sb, t.getUpdatedAt() == null ? null : t.getUpdatedAt().toString());

            // Close object
            sb.append('}');
            if (i < tasks.size() - 1) sb.append(',');
            sb.append('\n');
        }

        return sb.append(']').toString();
    }

    public static void appendJsonString(StringBuilder sb, String value) {
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
}
