package biz.gabrys.lesscss.compiler;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class ListWithoutEmptyValuesBuilderTest {

    private ListWithoutEmptyValuesBuilder builder;

    @Before
    public void init() {
        builder = new ListWithoutEmptyValuesBuilder();
    }

    @Test
    public void add_nullObject_notAdded() {
        final List<Object> values = builder.add(null).create();
        Assert.assertTrue("List should be empty", values.isEmpty());
    }

    @Test
    public void add_notNullObject_added() {
        final Object value = new Object();
        final List<Object> values = builder.add(value).create();
        Assert.assertEquals("List should store one object", 1, values.size());
        Assert.assertEquals("First value should be equal to set value", value, values.get(0));
    }

    @Test
    public void add_twoNotNullObjectAndOneNull_added() {
        final Object value1 = new Object();
        final Object value2 = new Object();
        final List<Object> values = builder.add(value1).add(null).add(value2).create();
        Assert.assertEquals("List should store two objects", 2, values.size());
        Assert.assertEquals("First value should be equal to set value1", value1, values.get(0));
        Assert.assertEquals("First value should be equal to set value2", value2, values.get(1));
    }

    @Test
    public void add_nullText_notAdded() {
        final List<Object> values = builder.add((String) null).create();
        Assert.assertTrue("List should be empty", values.isEmpty());
    }

    @Test
    public void add_notNullText_added() {
        final Object value = "value";
        final List<Object> values = builder.add(value).create();
        Assert.assertEquals("List should store one object", 1, values.size());
        Assert.assertEquals("First value should be equal to set value", value, values.get(0));
    }

    @Test
    public void add_twoNotNullTextAndOneNull_added() {
        final Object value1 = "value1";
        final Object value2 = "value2";
        final List<Object> values = builder.add(value1).add(null).add(value2).create();
        Assert.assertEquals("List should store two objects", 2, values.size());
        Assert.assertEquals("First value should be equal to set value1", value1, values.get(0));
        Assert.assertEquals("First value should be equal to set value2", value2, values.get(1));
    }
}
