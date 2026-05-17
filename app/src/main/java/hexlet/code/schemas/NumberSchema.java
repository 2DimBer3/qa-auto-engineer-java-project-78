package hexlet.code.schemas;

public final class NumberSchema extends BaseSchema<Integer> {
    private boolean positive = false;
    private Integer rangeMin = null;
    private Integer rangeMax = null;

    @Override
    public NumberSchema required() {
        super.required();
        return this;
    }

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
        // 1. Проверка на положительность числа
        if (positive && value <= 0) {
            return false;
        }

        // 2. Проверка на допустимый диапазон
        if (rangeMin != null && (value < rangeMin || value > rangeMax)) {
            return false;
        }

        return true;
    }
}
