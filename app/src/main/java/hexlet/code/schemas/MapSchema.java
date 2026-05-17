package hexlet.code.schemas;

import java.util.Map;

public final class MapSchema extends BaseSchema<Map<?, ?>, MapSchema> {
    private Integer size = null;
    private Map<String, BaseSchema<?, ?>> shapeSchemas = null;

    public MapSchema sizeof(int value) {
        this.size = value;
        return this;
    }

    public void shape(Map<String, BaseSchema<?, ?>> schemas) {
        this.shapeSchemas = schemas;
    }

    @Override
    protected boolean isNullValue(Map<?, ?> value) {
        return value == null;
    }

    @Override
    protected boolean check(Map<?, ?> value) {
        // 1. Проверка на размер Map
        if (size != null && value.size() != size) {
            return false;
        }

        // 2. Валидация значений по заданным ключам
        if (shapeSchemas != null) {
            for (Map.Entry<String, BaseSchema<?, ?>> entry : shapeSchemas.entrySet()) {
                String key = entry.getKey();
                BaseSchema<?, ?> schema = entry.getValue();
                Object fieldValue = value.get(key);

                @SuppressWarnings("unchecked")
                BaseSchema<Object, ?> rawSchema = (BaseSchema<Object, ?>) schema;
                if (!rawSchema.isValid(fieldValue)) {
                    return false;
                }
            }
        }

        return true;
    }
}
