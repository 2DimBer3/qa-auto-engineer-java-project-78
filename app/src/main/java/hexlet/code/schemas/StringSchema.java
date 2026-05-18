package hexlet.code.schemas;

public final class StringSchema extends BaseSchema<String> {

    private Integer minLength = null;
    private String substring = null;

    @Override
    public StringSchema required() {
        super.required();
        return this;
    }

    public StringSchema minLength(int length) {
        this.minLength = length;
        return this;
    }

    public StringSchema contains(String value) {
        this.substring = value;
        return this;
    }

    @Override
    protected boolean isNullValue(String str) {
        return str == null || str.isEmpty();
    }

    protected boolean check(String value) {
        // 1. Проверка минимальной длины
        if (minLength != null && value.length() < minLength) {
            return false;
        }

        // 2. Проверка наличия указанной подстроки
        if (substring != null && !value.contains(substring)) {
            return false;
        }

        return true;
    }
}
