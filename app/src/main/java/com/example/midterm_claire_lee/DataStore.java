package com.example.midterm_claire_lee;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class DataStore {
    private static final Set<Integer> history = new LinkedHashSet<>();
    private static final List<String> currentResults = new ArrayList<>();
    private static Integer lastNumber = null;

    private DataStore() {}

    public static void setCurrentTable(int n, List<String> values) {
        currentResults.clear();
        currentResults.addAll(values); // e.g., "7", "14", "21", ...
        lastNumber = n;
        history.add(n);
    }

    public static void updateCurrent(List<String> values) {
        currentResults.clear();
        currentResults.addAll(values);
    }

    public static List<String> getCurrentResults() {
        return new ArrayList<>(currentResults);
    }

    public static Integer getLastNumber() {
        return lastNumber;
    }

    public static List<Integer> getHistory() {
        return new ArrayList<>(history);
    }

    public static void clearCurrent() {
        currentResults.clear();
    }
}
