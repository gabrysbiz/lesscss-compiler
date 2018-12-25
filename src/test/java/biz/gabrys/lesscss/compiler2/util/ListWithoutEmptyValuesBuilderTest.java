package biz.gabrys.lesscss.compiler2.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public final class ListWithoutEmptyValuesBuilderTest {

    private ListWithoutEmptyValuesBuilder<Object> builder;

    @Before
    public void setup() {
        builder = spy(new ListWithoutEmptyValuesBuilder<>(value -> value != null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_checkerIsNull_throwsException() {
        new ListWithoutEmptyValuesBuilder<>(null);
    }

    @Test
    public void append_nullArray_notAppended() {
        final Object[] value = null;

        final List<Object> values = builder.append(value).build();

        assertThat(values).isEmpty();
        verify(builder).append(value);
        verify(builder).build();
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void append_emptyArray_notAppended() {
        final Object[] value = new Object[0];

        final List<Object> values = builder.append(value).build();

        assertThat(values).isEmpty();
        verify(builder).append(value);
        verify(builder).build();
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void append_arrayWithTwoNotNullTextAndOneNull_appended() {
        final Object value1 = new Object();
        final Object value2 = null;
        final Object value3 = new Object();
        final Object[] value = new Object[] { value1, value2, value3 };

        final List<Object> values = builder.append(value).build();

        assertThat(values).hasSize(2);
        assertThat(values.get(0)).isSameAs(value1);
        assertThat(values.get(1)).isSameAs(value3);
        verify(builder).append(value);
        verify(builder).build();
        verify(builder).append(value1);
        verify(builder).append(value2);
        verify(builder).append(value3);
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void append_nullText_notAppended() {
        final Object value = null;

        final List<Object> values = builder.append(value).build();

        assertThat(values).isEmpty();
    }

    @Test
    public void append_notNullText_appended() {
        final Object value = new Object();

        final List<Object> values = builder.append(value).build();

        assertThat(values).hasSize(1);
        assertThat(values.get(0)).isSameAs(value);
    }

    @Test
    public void append_twoNotNullTextAndOneNull_appended() {
        final Object value1 = new Object();
        final Object value2 = null;
        final Object value3 = new Object();

        final List<Object> values = builder.append(value1).append(value2).append(value3).build();

        assertThat(values).hasSize(2);
        assertThat(values.get(0)).isSameAs(value1);
        assertThat(values.get(1)).isSameAs(value3);
    }
}
