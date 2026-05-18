package hexlet.code;

import hexlet.code.schemas.StringSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringSchemaTest {

    private StringSchema schema;

    @BeforeEach
    void setUp() {
        schema = new Validator().string();
    }

    @Test
    void testDefaultBehavior() {
        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid("")).isTrue();
        assertThat(schema.isValid("any string")).isTrue();
    }

    @Test
    void testRequired() {
        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid("")).isFalse();
        assertThat(schema.isValid("non-empty")).isTrue();
    }

    @Test
    void testMinLength() {
        schema.minLength(5);

        assertThat(schema.isValid("12345")).isTrue();
        assertThat(schema.isValid("1234")).isFalse();
        assertThat(schema.isValid("long enough")).isTrue();
    }

    @Test
    void testMinLengthOverride() {
        // Последний вызов minLength имеет приоритет
        schema.minLength(10).minLength(4);

        assertThat(schema.isValid("Hexlet")).isTrue();
        assertThat(schema.isValid("Hi")).isFalse();
    }

    @Test
    void testContainsSingle() {
        schema.contains("hex");

        assertThat(schema.isValid("hexlet")).isTrue();
        assertThat(schema.isValid("Hexlet")).isFalse();
        assertThat(schema.isValid("no such word")).isFalse();
    }

    @Test
    void testContainsMultiple() {
        schema.contains("wt")
                .contains("what");

        // "wt" заменилось на "what"
        assertThat(schema.isValid("what does the fox say")).isTrue();
        assertThat(schema.isValid("wt does the fox say")).isFalse();

        schema.contains("whatthe");
        assertThat(schema.isValid("what does the fox say")).isFalse();

        // После добавления "whatthe" предыдущие ограничения остаются
        assertThat(schema.isValid("whatthe and what")).isTrue();
    }

    @Test
    void testCombinedConstraints() {
        schema.required()
                .minLength(5)
                .contains("hex");

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid("")).isFalse();
        assertThat(schema.isValid("hex")).isFalse();
        assertThat(schema.isValid("hexlet")).isTrue();
        assertThat(schema.isValid("Hexlet")).isFalse();
    }

    @Test
    void testFluentInterface() {
        boolean result = schema.required()
                .minLength(2)
                .contains("a")
                .contains("b")
                .isValid("ab");

        assertThat(result).isTrue();
    }
}
