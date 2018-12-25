package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LineNumbersValueTest {

    @Test
    public void toLineNumbersValue_allSupportedValues_success() {
        for (final LineNumbersValue value : LineNumbersValue.values()) {
            final LineNumbersValue result = LineNumbersValue.toLineNumbersValue(value.getValue());
            assertThat(result).isSameAs(value);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLineNumbersValue_valueIsNull_throwsException() {
        LineNumbersValue.toLineNumbersValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLineNumbersValue_valueIsUnsupported_throwsException() {
        LineNumbersValue.toLineNumbersValue("unsupported");
    }
}
