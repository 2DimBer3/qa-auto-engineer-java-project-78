package hexlet.code.schemas;

public abstract class BaseSchema<T, S extends BaseSchema<T, S>> {
    protected boolean required = false;

    @SuppressWarnings("unchecked")
    public S required() {
        this.required = true;
        return (S) this;
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
