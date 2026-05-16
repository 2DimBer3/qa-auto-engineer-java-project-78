package hexlet.code.schemas;

public final class NumberSchema extends BaseSchema<Integer, NumberSchema> {
    private boolean positive = false;
    private Integer rangeMin = null;
    private Integer rangeMax = null;

    public NumberSchema positive() {
        this.positive = true;
        return this;
    }

    public NumberSchema range(int min, int max) {
        this.rangeMin = min;
        this.rangeMax = max;
        return this;
    }

    @Override
    protected boolean isNullValue(Integer value) {
        return value == null;
    }

    @Override
    protected boolean check(Integer value) {
        if (positive && value <= 0) {
            return false;
        }

        if (rangeMin != null && (value < rangeMin || value > rangeMax)) {
            return false;
        }

        return true;
    }
}
