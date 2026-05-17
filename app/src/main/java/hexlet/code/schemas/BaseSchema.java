package hexlet.code.schemas;

public abstract class BaseSchema<T> {
    protected boolean required = false;

    public BaseSchema<T> required() {
        this.required = true;
        return this;
    }

    public boolean isValid(T value) {
        if (isNullValue(value)) {
            return !required;
        }
        return check(value);
    }

    protected abstract boolean isNullValue(T value);

    protected abstract boolean check(T value);
}
