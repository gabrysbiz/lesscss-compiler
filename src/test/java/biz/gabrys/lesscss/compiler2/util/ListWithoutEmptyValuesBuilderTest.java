package biz.gabrys.lesscss.compiler2.util;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import biz.gabrys.lesscss.compiler2.util.ListWithoutEmptyValuesBuilder.Checker;

public final class ListWithoutEmptyValuesBuilderTest {

    private ListWithoutEmptyValuesBuilder<Object> builder;

    @Before
    public void setup() {
        builder = Mockito.spy(new ListWithoutEmptyValuesBuilder<Object>(new Checker<Object>() {

            @Override
            public boolean isNotEmpty(final Object value) {
                return value != null;
            }
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_checkerIsNull_throwsException() {
        new ListWithoutEmptyValuesBuilder<Object>(null);
    }

    @Test
    public void append_nullArray_notAppended() {
        final Object[] value = null;

        final List<Object> values = builder.append(value).build();

        Assertions.assertThat(values).isEmpty();
        Mockito.verify(builder).append(value);
        Mockito.verify(builder).build();
        Mockito.verifyNoMoreInteractions(builder);
    }

    @Test
    public void append_emptyArray_notAppended() {
        final Object[] value = new Object[0];

        final List<Object> values = builder.append(value).build();

        Assertions.assertThat(values).isEmpty();
        Mockito.verify(builder).append(value);
        Mockito.verify(builder).build();
        Mockito.verifyNoMoreInteractions(builder);
    }

    @Test
    public void append_arrayWithTwoNotNullTextAndOneNull_appended() {
        final Object value1 = new Object();
        final Object value2 = null;
        final Object value3 = new Object();
        final Object[] value = new Object[] { value1, value2, value3 };

        final List<Object> values = builder.append(value).build();

        Assertions.assertThat(values).hasSize(2);
        Assertions.assertThat(values.get(0)).isSameAs(value1);
        Assertions.assertThat(values.get(1)).isSameAs(value3);
        Mockito.verify(builder).append(value);
        Mockito.verify(builder).build();
        Mockito.verify(builder).append(value1);
        Mockito.verify(builder).append(value2);
        Mockito.verify(builder).append(value3);
        Mockito.verifyNoMoreInteractions(builder);
    }

    @Test
    public void append_nullText_notAppended() {
        final Object value = null;

        final List<Object> values = builder.append(value).build();

        Assertions.assertThat(values).isEmpty();
    }

    @Test
    public void append_notNullText_appended() {
        final Object value = new Object();

        final List<Object> values = builder.append(value).build();

        Assertions.assertThat(values).hasSize(1);
        Assertions.assertThat(values.get(0)).isSameAs(value);
    }

    @Test
    public void append_twoNotNullTextAndOneNull_appended() {
        final Object value1 = new Object();
        final Object value2 = null;
        final Object value3 = new Object();

        final List<Object> values = builder.append(value1).append(value2).append(value3).build();

        Assertions.assertThat(values).hasSize(2);
        Assertions.assertThat(values.get(0)).isSameAs(value1);
        Assertions.assertThat(values.get(1)).isSameAs(value3);
    }
}
