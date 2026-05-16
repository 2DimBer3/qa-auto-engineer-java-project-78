package hexlet.code;

import hexlet.code.schemas.StringSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void testDefaultBehavior() {
        StringSchema schema = validator.string();

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid("")).isTrue();
        assertThat(schema.isValid("any string")).isTrue();
    }

    @Test
    void testRequired() {
        StringSchema schema = validator.string();
        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid("")).isFalse();
        assertThat(schema.isValid("non-empty")).isTrue();
    }

    @Test
    void testMinLength() {
        StringSchema schema = validator.string();
        schema.minLength(5);

        assertThat(schema.isValid("12345")).isTrue();
        assertThat(schema.isValid("1234")).isFalse();
        assertThat(schema.isValid("long enough")).isTrue();
    }

    @Test
    void testMinLengthOverride() {
        // Последний вызов minLength имеет приоритет
        StringSchema schema = validator.string();
        schema.minLength(10).minLength(4);

        assertThat(schema.isValid("Hexlet")).isTrue();
        assertThat(schema.isValid("Hi")).isFalse();
    }

    @Test
    void testContainsSingle() {
        StringSchema schema = validator.string();
        schema.contains("hex");

        assertThat(schema.isValid("hexlet")).isTrue();
        assertThat(schema.isValid("Hexlet")).isFalse();
        assertThat(schema.isValid("no such word")).isFalse();
    }

    @Test
    void testContainsMultiple() {
        StringSchema schema = validator.string();
        schema.contains("wh");
        schema.contains("what");

        // Должны присутствовать обе подстроки
        assertThat(schema.isValid("what does the fox say")).isTrue();

        schema.contains("whatthe");
        assertThat(schema.isValid("what does the fox say")).isFalse();

        // После добавления "whatthe" предыдущие ограничения остаются
        assertThat(schema.isValid("whatthe and what")).isTrue();
    }

    @Test
    void testCombinedConstraints() {
        StringSchema schema = validator.string()
                .required()
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
        StringSchema schema = validator.string()
                .required()
                .minLength(2)
                .contains("a");

        assertThat(schema.contains("b").isValid("ab")).isTrue();
        assertThat(schema.isValid("a")).isFalse();
    }
}
