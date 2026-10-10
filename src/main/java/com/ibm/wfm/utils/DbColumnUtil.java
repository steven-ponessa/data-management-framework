package com.ibm.wfm.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.ibm.wfm.annotations.DbColumn;

public final class DbColumnUtil {

    private DbColumnUtil() {}

    public static List<String> getNaturalKeyColumnNames(Class<?> type) {
    	
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DbColumn.class))
                .map(field -> field.getAnnotation(DbColumn.class))
                .filter(dbColumn -> dbColumn.keySeq() > 0)
                .sorted(Comparator.comparingInt(DbColumn::keySeq))
                .map(DbColumn::columnName)
                .toList();
    }
}