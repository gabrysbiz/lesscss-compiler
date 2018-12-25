package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LessVariableOptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void construct_nameIsNull_throwsException() {
        new LessVariableOption(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_nameIsBlank_throwsException() {
        new LessVariableOption(" \r\t  ", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_valueIsNull_throwsException() {
        new LessVariableOption("name", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_valueIsBlank_throwsException() {
        new LessVariableOption("name", " \r\t  ");
    }

    @Test
    public void construct_nameAndValueAreNotBlank_createdSuccessfully() {
        final String name = "name";
        final String value = " value ";

        final LessVariableOption variable = new LessVariableOption(name, value);

        assertThat(variable.getName()).isSameAs(name);
        assertThat(variable.getValue()).isSameAs(value);
    }

    @Test
    public void hashCode_variablesAreTheSame_returnsTheSameNumber() {
        final LessVariableOption variable1 = new LessVariableOption("name", "value");
        final LessVariableOption variable2 = new LessVariableOption("name", "value");

        final int hashCode1 = variable1.hashCode();
        final int hashCode2 = variable2.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    public void hashCode_variablesAreNotTheSame_returnsDifferentNumbers() {
        final LessVariableOption variable1 = new LessVariableOption("name", "value");
        final LessVariableOption variable2 = new LessVariableOption("value", "name");

        final int hashCode1 = variable1.hashCode();
        final int hashCode2 = variable2.hashCode();

        assertThat(hashCode1).isNotEqualTo(hashCode2);
    }

    @Test
    public void equals_variablesAreTheSame_returnsTrue() {
        final LessVariableOption variable1 = new LessVariableOption("name", "value");
        final LessVariableOption variable2 = new LessVariableOption("name", "value");

        assertThat(variable1.equals(variable1)).isTrue();
        assertThat(variable1.equals(variable2)).isTrue();
        assertThat(variable2.equals(variable1)).isTrue();
    }

    @Test
    public void equals_variablesAreNotTheSame_returnsFalse() {
        final LessVariableOption variable = new LessVariableOption("name", "value");

        assertThat(variable.equals(new LessVariableOption("value", "name"))).isFalse();
        assertThat(variable.equals(new LessVariableOption("name", "name"))).isFalse();
        assertThat(variable.equals(new LessVariableOption("value", "value"))).isFalse();
        assertThat(variable.equals(new Object())).isFalse();
        assertThat(variable.equals(null)).isFalse();
    }

    @Test
    public void testToString() {
        final LessVariableOption variable = new LessVariableOption("name", "value");
        assertThat(variable.toString()).isEqualTo("name=value");
    }
}
