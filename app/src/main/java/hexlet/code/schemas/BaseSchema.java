package hexlet.code.schemas;

public abstract class BaseSchema<T> {
    protected boolean required = false;

    /**
     * Делает значение обязательным (не допускается пустое значение).
     *
     * <p>Подклассы могут переопределять этот метод для поддержки fluent-интерфейса,
     * возвращая конкретный тип схемы. При переопределении обязательно вызвать
     * {@code super.required()}.
     *
     * @return this (или экземпляр подкласса)
     */
    public BaseSchema<T> required() {
        this.required = true;
        return this;
    }

    public final boolean isValid(T value) {
        if (isNullValue(value)) {
            return !required;
        }
        return check(value);
    }

    protected abstract boolean isNullValue(T value);

    protected abstract boolean check(T value);
}
