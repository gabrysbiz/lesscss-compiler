package biz.gabrys.lesscss.compiler2.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public final class StringUtilsTest {

    @Test
    public void isBlank_null_returnsTrue() {
        final boolean result = StringUtils.isBlank(null);
        assertThat(result).isTrue();
    }

    @Test
    public void isNotBlank_null_returnsFalse() {
        final boolean result = StringUtils.isNotBlank(null);
        assertThat(result).isFalse();
    }

    @Test
    public void isBlank_empty_returnsTrue() {
        final boolean result = StringUtils.isBlank("");
        assertThat(result).isTrue();
    }

    @Test
    public void isNotBlank_empty_returnsFalse() {
        final boolean result = StringUtils.isNotBlank("");
        assertThat(result).isFalse();
    }

    @Test
    public void isBlank_onlyWhitespaces_returnsTrue() {
        final boolean result = StringUtils.isBlank(" \t\r\n");
        assertThat(result).isTrue();
    }

    @Test
    public void isNotBlank_onlyWhitespaces_returnsFalse() {
        final boolean result = StringUtils.isNotBlank(" \t\r\n");
        assertThat(result).isFalse();
    }

    @Test
    public void isBlank_onlyLetters_returnsFalse() {
        final boolean result = StringUtils.isBlank("abc");
        assertThat(result).isFalse();
    }

    @Test
    public void isNotBlank_onlyLetters_returnsTrue() {
        final boolean result = StringUtils.isNotBlank("abc");
        assertThat(result).isTrue();
    }

    @Test
    public void isBlank_lettersAndWhitespaces_returnsFalse() {
        final boolean result = StringUtils.isBlank("  a b c  ");
        assertThat(result).isFalse();
    }

    @Test
    public void isNotBlank_lettersAndWhitespaces_returnsTrue() {
        final boolean result = StringUtils.isNotBlank("  a b c  ");
        assertThat(result).isTrue();
    }

    @Test
    public void toStringIfNotNull_objectIsNotNull_returnsString() {
        final String result = StringUtils.toStringIfNotNull(new StringBuilder("text"));
        assertThat(result).isEqualTo("text");
    }

    @Test
    public void toStringIfNotNull_objectIsNull_returnsNull() {
        final String result = StringUtils.toStringIfNotNull(null);
        assertThat(result).isNull();
    }

    @Test
    public void defaultText_textIsNotNull_returnsText() {
        final String text = "text";
        final String defaultText = "defaultText";

        final String result = StringUtils.defaultString(text, defaultText);
        assertThat(result).isSameAs(text);
    }

    @Test
    public void defaultText_textIsNull_returnsDefaultText() {
        final String defaultText = "defaultText";

        final String result = StringUtils.defaultString(null, defaultText);
        assertThat(result).isSameAs(defaultText);
    }
}
