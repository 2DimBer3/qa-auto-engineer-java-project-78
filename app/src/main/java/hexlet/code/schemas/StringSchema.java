package hexlet.code.schemas;

import java.util.LinkedHashSet;
import java.util.Set;

public final class StringSchema extends BaseSchema<String> {

    private Integer minLength = null;
    private final Set<String> substrings = new LinkedHashSet<>();

    @Override
    public StringSchema required() {
        super.required();
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

    @Override
    protected boolean isNullValue(String str) {
        return str == null || str.isEmpty();
    }

    protected boolean check(String str) {
        // 1. Проверка минимальной длины
        if (minLength != null && str.length() < minLength) {
            return false;
        }

        // 2. Проверка наличия всех указанных подстрок
        for (String sub : substrings) {
            if (!str.contains(sub)) {
                return false;
            }
        }

        return true;
    }
}
