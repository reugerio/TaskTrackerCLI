package util;

import model.Task;

import java.util.List;

public class JsonHelper {

    public static String buildJsonString(List<Task> tasks) {
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

        return sb.toString();
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
