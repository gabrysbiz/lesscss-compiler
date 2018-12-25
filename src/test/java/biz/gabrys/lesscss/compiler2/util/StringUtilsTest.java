package biz.gabrys.lesscss.compiler2.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public final class StringUtilsTest {

    @Test
    public void isBlank_null_returnsTrue() {
        final boolean result = StringUtils.isBlank(null);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void isNotBlank_null_returnsFalse() {
        final boolean result = StringUtils.isNotBlank(null);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void isBlank_empty_returnsTrue() {
        final boolean result = StringUtils.isBlank("");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void isNotBlank_empty_returnsFalse() {
        final boolean result = StringUtils.isNotBlank("");
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void isBlank_onlyWhitespaces_returnsTrue() {
        final boolean result = StringUtils.isBlank(" \t\r\n");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void isNotBlank_onlyWhitespaces_returnsFalse() {
        final boolean result = StringUtils.isNotBlank(" \t\r\n");
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void isBlank_onlyLetters_returnsFalse() {
        final boolean result = StringUtils.isBlank("abc");
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void isNotBlank_onlyLetters_returnsTrue() {
        final boolean result = StringUtils.isNotBlank("abc");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void isBlank_lettersAndWhitespaces_returnsFalse() {
        final boolean result = StringUtils.isBlank("  a b c  ");
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void isNotBlank_lettersAndWhitespaces_returnsTrue() {
        final boolean result = StringUtils.isNotBlank("  a b c  ");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void toStringIfNotNull_objectIsNotNull_returnsString() {
        final String result = StringUtils.toStringIfNotNull(new StringBuilder("text"));
        Assertions.assertThat(result).isEqualTo("text");
    }

    @Test
    public void toStringIfNotNull_objectIsNull_returnsNull() {
        final String result = StringUtils.toStringIfNotNull(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void defaultText_textIsNotNull_returnsText() {
        final String text = "text";
        final String defaultText = "defaultText";

        final String result = StringUtils.defaultString(text, defaultText);
        Assertions.assertThat(result).isSameAs(text);
    }

    @Test
    public void defaultText_textIsNull_returnsDefaultText() {
        final String defaultText = "defaultText";

        final String result = StringUtils.defaultString(null, defaultText);
        Assertions.assertThat(result).isSameAs(defaultText);
    }
}
