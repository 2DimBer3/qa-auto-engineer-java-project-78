package hexlet.code;

import hexlet.code.schemas.MapSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapSchemaTest {
    private MapSchema schema;

    @BeforeEach
    void setUp() {
        schema = new Validator().map();
    }

    @Test
    void testDefaultBehavior() {
        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(new HashMap<>())).isTrue();
    }

    @Test
    void testRequired() {
        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(new HashMap<>())).isTrue();

        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testSizeof() {
        schema.sizeof(2);

        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        assertThat(schema.isValid(data)).isFalse();

        data.put("key2", "value2");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testRequiredAndSizeof() {
        schema.required()
                .sizeof(2);

        assertThat(schema.isValid(null)).isFalse();

        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        assertThat(schema.isValid(data)).isFalse();

        data.put("key2", "value2");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testSizeofZero() {
        schema.sizeof(0);

        assertThat(schema.isValid(new HashMap<>())).isTrue();

        Map<String, String> data = new HashMap<>();
        data.put("k", "v");
        assertThat(schema.isValid(data)).isFalse();
    }

    @Test
    void testFluentChaining() {
        Map<String, String> data = new HashMap<>();
        data.put("a", "b");

        boolean result = schema
                .required()
                .sizeof(1)
                .isValid(data);

        assertThat(result).isTrue();
    }
}
