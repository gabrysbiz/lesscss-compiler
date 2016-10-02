package biz.gabrys.lesscss.compiler;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void isNotBlank_null_false() {
        Assert.assertFalse("Null should be blank", StringUtils.isNotBlank(null));
    }

    @Test
    public void isNotBlank_empty_false() {
        Assert.assertFalse("Empty text should be blank", StringUtils.isNotBlank(""));
    }

    @Test
    public void isNotBlank_onlyWhitespaces_false() {
        Assert.assertFalse("Text \" \\t\\r\\n\" should be blank", StringUtils.isNotBlank(" \t\r\n"));
    }

    @Test
    public void isNotBlank_onlyLetters_true() {
        Assert.assertTrue("Text \"abc\" shouldn't be blank", StringUtils.isNotBlank("abc"));
    }

    @Test
    public void isNotBlank_lettersAndWhitespaces_true() {
        Assert.assertTrue("Text \"  a b c  \" shouldn't be blank", StringUtils.isNotBlank("  a b c  "));
    }
}
