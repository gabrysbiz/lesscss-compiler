package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LessVariableTest {

    @Test(expected = IllegalArgumentException.class)
    public void construct_nameIsNull_throwsException() {
        new LessVariable(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_nameIsBlank_throwsException() {
        new LessVariable(" \r\t  ", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_valueIsNull_throwsException() {
        new LessVariable("name", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_valueIsBlank_throwsException() {
        new LessVariable("name", " \r\t  ");
    }

    @Test
    public void construct_nameAndValueAreNotBlank_createdSuccessfully() {
        final String name = "name";
        final String value = " value ";

        final LessVariable variable = new LessVariable(name, value);

        assertThat(variable.getName()).isSameAs(name);
        assertThat(variable.getValue()).isSameAs(value);
    }

    @Test
    public void hashCode_variablesAreTheSame_returnsTheSameNumber() {
        final LessVariable variable1 = new LessVariable("name", "value");
        final LessVariable variable2 = new LessVariable("name", "value");

        final int hashCode1 = variable1.hashCode();
        final int hashCode2 = variable2.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    public void hashCode_variablesAreNotTheSame_returnsDifferentNumbers() {
        final LessVariable variable1 = new LessVariable("name", "value");
        final LessVariable variable2 = new LessVariable("value", "name");

        final int hashCode1 = variable1.hashCode();
        final int hashCode2 = variable2.hashCode();

        assertThat(hashCode1).isNotEqualTo(hashCode2);
    }

    @Test
    public void equals_variablesAreTheSame_returnsTrue() {
        final LessVariable variable1 = new LessVariable("name", "value");
        final LessVariable variable2 = new LessVariable("name", "value");

        assertThat(variable1.equals(variable2)).isTrue();
        assertThat(variable2.equals(variable1)).isTrue();
    }

    @Test
    public void hashCode_variablesAreNotTheSame_returnsFalse() {
        final LessVariable variable = new LessVariable("name", "value");

        assertThat(variable.equals(new LessVariable("value", "name"))).isFalse();
        assertThat(variable.equals(new LessVariable("name", "name"))).isFalse();
        assertThat(variable.equals(new LessVariable("value", "value"))).isFalse();
        assertThat(variable.equals(new Object())).isFalse();
        assertThat(variable.equals(null)).isFalse();
    }

    @Test
    public void testToString() {
        final LessVariable variable = new LessVariable("name", "value");
        assertThat(variable.toString()).isEqualTo("name=value");
    }
}
