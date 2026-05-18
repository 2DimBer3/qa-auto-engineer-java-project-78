package hexlet.code;

import hexlet.code.schemas.NumberSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberSchemaTest {
    private NumberSchema schema;

    @BeforeEach
    void setUp() {
        schema = new Validator().number();
    }

    @Test
    void testDefaultBehavior() {
        assertThat(schema.isValid(5)).isTrue();
        assertThat(schema.isValid(null)).isTrue();
    }

    @Test
    void testRequired() {
        schema.required();
        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(10)).isTrue();
    }

    @Test
    void testPositive() {
        schema.positive();

        // positive без required
        assertThat(schema.isValid(null)).isTrue();

        // positive c required
        schema.required();
        assertThat(schema.isValid(null)).isFalse();

        assertThat(schema.isValid(10)).isTrue();
        assertThat(schema.isValid(-5)).isFalse();
        assertThat(schema.isValid(0)).isFalse();
    }

    @Test
    void testPositiveOverride() {
        schema.positive().positive();
        assertThat(schema.isValid(5)).isTrue();
        assertThat(schema.isValid(-5)).isFalse();
    }

    @Test
    void testRange() {
        schema.range(5, 10);

        assertThat(schema.isValid(5)).isTrue();
        assertThat(schema.isValid(10)).isTrue();
        assertThat(schema.isValid(4)).isFalse();
        assertThat(schema.isValid(11)).isFalse();
    }

    @Test
    void testRangeOverride() {
        schema.range(1, 5)
                .range(10, 20);
        assertThat(schema.isValid(15)).isTrue();
        assertThat(schema.isValid(3)).isFalse();
    }

    @Test
    void testCombinedConstraints() {
        schema.required()
                .positive()
                .range(2, 8);

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(0)).isFalse();
        assertThat(schema.isValid(-1)).isFalse();
        assertThat(schema.isValid(1)).isFalse();
        assertThat(schema.isValid(2)).isTrue();
        assertThat(schema.isValid(8)).isTrue();
        assertThat(schema.isValid(9)).isFalse();
    }

    @Test
    void testFluentChaining() {
        boolean result = schema.required()
                .positive()
                .range(1, 100)
                .isValid(42);

        assertThat(result).isTrue();
    }
}
