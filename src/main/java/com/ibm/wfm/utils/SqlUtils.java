package com.ibm.wfm.utils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class SqlUtils {

    public static String formatSql(String sql, List<Object> params) {
        if (params == null || params.isEmpty()) {
            return sql;
        }

        StringBuilder sb = new StringBuilder();
        int paramIndex = 0;
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '?' && paramIndex < params.size()) {
                Object param = params.get(paramIndex++);
                sb.append(formatParam(param));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String formatParam(Object param) {
        if (param == null) {
            return "NULL";
        } else if (param instanceof String || param instanceof java.sql.Date ||
                   param instanceof java.sql.Timestamp) {
            return "'" + param.toString().replace("'", "''") + "'";
        } else {
            return param.toString();
        }
    }

    // Example usage
    public static void main(String[] args) {
        String sql = "SELECT * FROM users WHERE id = ? AND status = ? AND created_at > ?";
        List<Object> params = Arrays.asList(42, "ACTIVE", Timestamp.valueOf("2023-09-01 10:00:00"));

        System.out.println(formatSql(sql, params));
    }
}