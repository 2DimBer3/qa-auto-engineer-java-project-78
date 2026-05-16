package hexlet.code.schemas;

import java.util.Map;

public final class MapSchema extends BaseSchema<Map<?, ?>, MapSchema> {
    private Integer size = null;

    public MapSchema sizeof(int value) {
        this.size = value;
        return this;
    }

    @Override
    protected boolean isNullValue(Map<?, ?> value) {
        return value == null;
    }

    @Override
    protected boolean check(Map<?, ?> value) {
        // 1. Проверка на размер Map
        return size == null || value.size() == size;
    }
}
