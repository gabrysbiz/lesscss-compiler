package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

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
    public void testEqualsAndHashCodeContracts() {
        EqualsVerifier.forClass(LessVariableOption.class).usingGetClass().withNonnullFields("name", "value").verify();
    }

    @Test
    public void testToString() {
        final LessVariableOption variable = new LessVariableOption("name", "value");
        assertThat(variable.toString()).isEqualTo("name=value");
    }
}
