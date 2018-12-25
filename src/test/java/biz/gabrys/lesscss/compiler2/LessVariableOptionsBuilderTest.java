package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LessVariableOptionsBuilderTest {

    @Spy
    private LessVariableOptionsBuilder builder;

    @Test(expected = IllegalArgumentException.class)
    public void append_mapIsNull_throwsException() {
        builder.append((Map<String, String>) null);
    }

    @Test
    public void append_mapIsNotNull_variablesAreAdded() {
        final Map<String, String> variables = new LinkedHashMap<>();
        variables.put("name1", "value1");
        variables.put("name2", "value2");

        final LessVariableOptionsBuilder result = builder.append(variables);

        assertThat(result).isSameAs(builder);
        verify(builder).append("name1", "value1");
        verify(builder).append("name2", "value2");
        assertThat(builder.getVariables()).containsExactly(new LessVariableOption("name1", "value1"),
                new LessVariableOption("name2", "value2"));
    }

    @Test
    public void append_nameIsValue_variableIsAdded() {
        final LessVariableOptionsBuilder result = builder.append("name", "value");

        assertThat(result).isSameAs(builder);
        assertThat(builder.getVariables()).containsExactly(new LessVariableOption("name", "value"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void append_iterableIsNull_throwsException() {
        builder.append((Iterable<LessVariableOption>) null);
    }

    @Test
    public void append_iterableIsNotNull_variablesAreAdded() {
        final LessVariableOption variable1 = new LessVariableOption("name1", "value1");
        final LessVariableOption variable2 = new LessVariableOption("name2", "value2");

        final LessVariableOptionsBuilder result = builder.append(Arrays.asList(variable1, variable2));

        assertThat(result).isSameAs(builder);
        verify(builder).append(variable1);
        verify(builder).append(variable2);
        assertThat(builder.getVariables()).containsExactly(variable1, variable2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_arrayIsNull_throwsException() {
        builder.append((LessVariableOption[]) null);
    }

    @Test
    public void append_arrayIsNotNull_variablesAreAdded() {
        final LessVariableOption variable1 = new LessVariableOption("name1", "value1");
        final LessVariableOption variable2 = new LessVariableOption("name2", "value2");

        final LessVariableOptionsBuilder result = builder.append(new LessVariableOption[] { variable1, variable2 });

        assertThat(result).isSameAs(builder);
        verify(builder).append(variable1);
        verify(builder).append(variable2);
        assertThat(builder.getVariables()).containsExactly(variable1, variable2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_variableIsNull_throwsException() {
        builder.append((LessVariableOption) null);
    }

    @Test
    public void append_variableIsNotNull_variablesAreAdded() {
        final LessVariableOption variable = new LessVariableOption("name", "value");

        final LessVariableOptionsBuilder result = builder.append(variable);

        assertThat(result).isSameAs(builder);
        assertThat(builder.getVariables()).containsExactly(variable);
    }

    @Test
    public void build() {
        final Map<String, String> variablesMap = new LinkedHashMap<>();
        variablesMap.put("name1", "value1");
        variablesMap.put("name2", "value2");

        final List<LessVariableOption> variables = builder //
                .append(variablesMap) //
                .append("name3", "value3") //
                .append(new LessVariableOption("name2", "value4")) //
                .build();

        assertThat(variables).containsExactly(//
                new LessVariableOption("name1", "value1"), //
                new LessVariableOption("name2", "value2"), //
                new LessVariableOption("name3", "value3"), //
                new LessVariableOption("name2", "value4"));
    }
}
