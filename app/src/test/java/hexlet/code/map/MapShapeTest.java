package hexlet.code.map;

import hexlet.code.Validator;
import hexlet.code.schemas.BaseSchema;
import hexlet.code.schemas.MapSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapShapeTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void testShapeBasic() {
        MapSchema schema = validator.map();

        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("firstName", validator.string().required());
        schemas.put("lastName", validator.string().required().minLength(2));
        schema.shape(schemas);

        Map<String, String> human1 = new HashMap<>();
        human1.put("firstName", "John");
        human1.put("lastName", "Smith");
        assertThat(schema.isValid(human1)).isTrue();

        Map<String, String> human2 = new HashMap<>();
        human2.put("firstName", "John");
        human2.put("lastName", null);
        assertThat(schema.isValid(human2)).isFalse();

        Map<String, String> human3 = new HashMap<>();
        human3.put("firstName", "Anna");
        human3.put("lastName", "B");
        assertThat(schema.isValid(human3)).isFalse();
    }

    @Test
    void testShapeMissingKey() {
        MapSchema schema = validator.map();
        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("name", validator.string().required());
        schema.shape(schemas);

        Map<String, String> data = new HashMap<>();
        assertThat(schema.isValid(data)).isFalse();
    }

    @Test
    void testShapeWithoutRequiredOnSchema() {
        MapSchema schema = validator.map();
        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("age", validator.number().positive());
        schema.shape(schemas);

        Map<String, Object> data = new HashMap<>();
        data.put("age", 25);
        assertThat(schema.isValid(data)).isTrue();

        data.put("age", -5);
        assertThat(schema.isValid(data)).isFalse();

        data.put("age", null);
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testShapeCombinedWithRequiredAndSizeof() {
        MapSchema schema = validator.map().required().sizeof(2);
        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("firstName", validator.string().required());
        schemas.put("lastName", validator.string().required().minLength(2));
        schema.shape(schemas);

        // Несоответствие размера
        Map<String, String> data = new HashMap<>();
        data.put("firstName", "John");
        assertThat(schema.isValid(data)).isFalse();

        // Удовлетворяет размеру
        data.put("lastName", "Smith");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testShapeOverridesPrevious() {
        MapSchema schema = validator.map();
        Map<String, BaseSchema<?>> schemas1 = new HashMap<>();
        schemas1.put("key", validator.string().minLength(10));
        schema.shape(schemas1);

        Map<String, BaseSchema<?>> schemas2 = new HashMap<>();
        schemas2.put("key", validator.string().minLength(2));
        schema.shape(schemas2); // перезаписывает

        Map<String, String> data = new HashMap<>();
        data.put("key", "Hi");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testShapeWithEmptySchemas() {
        MapSchema schema = validator.map();
        schema.shape(new HashMap<>()); // нет ограничений для ключей

        Map<String, String> data = new HashMap<>();
        data.put("anything", "value");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testShapeWithMultipleTypes() {
        MapSchema schema = validator.map();
        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("name", validator.string().required().contains("Smith"));
        schemas.put("age", validator.number().positive().range(18, 120));
        schema.shape(schemas);

        Map<String, Object> validData = new HashMap<>();
        validData.put("name", "John Smith");
        validData.put("age", 30);
        assertThat(schema.isValid(validData)).isTrue();

        Map<String, Object> invalidName = new HashMap<>();
        invalidName.put("name", "John Doe");
        invalidName.put("age", 30);
        assertThat(schema.isValid(invalidName)).isFalse();

        Map<String, Object> invalidAge = new HashMap<>();
        invalidAge.put("name", "John Smith");
        invalidAge.put("age", 15);
        assertThat(schema.isValid(invalidAge)).isFalse();
    }

    @Test
    void testShapeDoesNotValidateExtraKeys() {
        MapSchema schema = validator.map();
        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("requiredField", validator.string().required());
        schema.shape(schemas);

        Map<String, String> data = new HashMap<>();
        data.put("requiredField", "value");
        data.put("extraField", "anything"); // extraField не проверяется
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testShapeWithNullKeyValue() {
        MapSchema schema = validator.map();
        Map<String, BaseSchema<?>> schemas = new HashMap<>();
        schemas.put("nullableField", validator.string()); // без required
        schema.shape(schemas);

        Map<String, String> data = new HashMap<>();
        data.put("nullableField", null);
        assertThat(schema.isValid(data)).isTrue();

        // Добавим required
        schemas.put("nullableField", validator.string().required());
        schema.shape(schemas);
        assertThat(schema.isValid(data)).isFalse();
    }
}
