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
public class LessVariablesOptionBuilderTest {

    @Spy
    private LessVariablesOptionBuilder builder;

    @Test(expected = IllegalArgumentException.class)
    public void append_mapIsNull_throwsException() {
        builder.append((Map<String, String>) null);
    }

    @Test
    public void append_mapIsNotNull_variablesAreAdded() {
        final Map<String, String> variables = new LinkedHashMap<String, String>();
        variables.put("name1", "value1");
        variables.put("name2", "value2");

        final LessVariablesOptionBuilder result = builder.append(variables);

        assertThat(result).isSameAs(builder);
        verify(builder).append("name1", "value1");
        verify(builder).append("name2", "value2");
        assertThat(builder.getVariables()).containsExactly(new LessVariable("name1", "value1"), new LessVariable("name2", "value2"));
    }

    @Test
    public void append_nameIsValue_variableIsAdded() {
        final LessVariablesOptionBuilder result = builder.append("name", "value");

        assertThat(result).isSameAs(builder);
        assertThat(builder.getVariables()).containsExactly(new LessVariable("name", "value"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void append_iterableIsNull_throwsException() {
        builder.append((Iterable<LessVariable>) null);
    }

    @Test
    public void append_iterableIsNotNull_variablesAreAdded() {
        final LessVariable variable1 = new LessVariable("name1", "value1");
        final LessVariable variable2 = new LessVariable("name2", "value2");

        final LessVariablesOptionBuilder result = builder.append(Arrays.asList(variable1, variable2));

        assertThat(result).isSameAs(builder);
        verify(builder).append(variable1);
        verify(builder).append(variable2);
        assertThat(builder.getVariables()).containsExactly(variable1, variable2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_arrayIsNull_throwsException() {
        builder.append((LessVariable[]) null);
    }

    @Test
    public void append_arrayIsNotNull_variablesAreAdded() {
        final LessVariable variable1 = new LessVariable("name1", "value1");
        final LessVariable variable2 = new LessVariable("name2", "value2");

        final LessVariablesOptionBuilder result = builder.append(new LessVariable[] { variable1, variable2 });

        assertThat(result).isSameAs(builder);
        verify(builder).append(variable1);
        verify(builder).append(variable2);
        assertThat(builder.getVariables()).containsExactly(variable1, variable2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_variableIsNull_throwsException() {
        builder.append((LessVariable) null);
    }

    @Test
    public void append_variableIsNotNull_variablesAreAdded() {
        final LessVariable variable = new LessVariable("name", "value");

        final LessVariablesOptionBuilder result = builder.append(variable);

        assertThat(result).isSameAs(builder);
        assertThat(builder.getVariables()).containsExactly(variable);
    }

    @Test
    public void build() {
        final Map<String, String> variablesMap = new LinkedHashMap<String, String>();
        variablesMap.put("name1", "value1");
        variablesMap.put("name2", "value2");

        final List<LessVariable> variables = builder //
                .append(variablesMap) //
                .append("name3", "value3") //
                .append(new LessVariable("name2", "value4")) //
                .build();

        assertThat(variables).containsExactly(//
                new LessVariable("name1", "value1"), //
                new LessVariable("name2", "value2"), //
                new LessVariable("name3", "value3"), //
                new LessVariable("name2", "value4"));
    }
}
