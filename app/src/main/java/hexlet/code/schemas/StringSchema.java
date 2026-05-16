package hexlet.code.schemas;

import java.util.LinkedHashSet;
import java.util.Set;

public final class StringSchema {

    private boolean required = false;
    private Integer minLength = null;
    private final Set<String> substrings = new LinkedHashSet<>();

    public StringSchema required() {
        this.required = true;
        return this;
    }

    public StringSchema minLength(int length) {
        this.minLength = length;
        return this;
    }

    public StringSchema contains(String substring) {
        this.substrings.add(substring);
        return this;
    }

    public boolean isValid(String str) {
        // 1. Проверка обязательности
        if (str == null || str.isEmpty()) {
            return !required;
        }

        // 2. Проверка минимальной длины
        if (minLength != null && str.length() < minLength) {
            return false;
        }

        // 3. Проверка наличия всех указанных подстрок
        for (String sub : substrings) {
            if (!str.contains(sub)) {
                return false;
            }
        }

        return true;
    }
}
