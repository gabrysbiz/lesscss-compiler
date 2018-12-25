package biz.gabrys.lesscss.compiler2.filesystem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FileDataTest {

    @Test
    public void hashCode_objectsAreTheSame_returnsTheSameNumber() {
        final byte[] content = new byte[] { 7, 4, 12 };
        final String encoding = "encoding";

        final FileData data1 = new FileData(content, encoding);
        final FileData data2 = new FileData(content, encoding);

        final int hashCode1 = data1.hashCode();
        final int hashCode2 = data2.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    public void hashCode_objectsHaveDifferentContent_returnsDifferentNumbers() {
        final String encoding = "encoding";

        final FileData data1 = new FileData(new byte[] { 0, 12, 34 }, encoding);
        final FileData data2 = new FileData(new byte[] { 1, 2, 3 }, encoding);

        final int hashCode1 = data1.hashCode();
        final int hashCode2 = data2.hashCode();

        assertThat(hashCode1).isNotEqualTo(hashCode2);
    }

    @Test
    public void equals_objectsHaveDifferentEncoding_returnsDifferentNumbers() {
        final byte[] content = new byte[] { 5, 6, 7 };

        final FileData data1 = new FileData(content, null);
        final FileData data2 = new FileData(content, "encoding");

        final int hashCode1 = data1.hashCode();
        final int hashCode2 = data2.hashCode();

        assertThat(hashCode1).isNotEqualTo(hashCode2);
    }

    @Test
    public void equals_objectsAreTheSame_returnsTrue() {
        final byte[] content = new byte[] { 7, 4, 12 };
        final String encoding = "encoding";

        final FileData data1 = new FileData(content, encoding);
        final FileData data2 = new FileData(content, encoding);

        assertThat(data1.equals(data1)).isTrue();
        assertThat(data1.equals(data2)).isTrue();
        assertThat(data2.equals(data1)).isTrue();
    }

    @Test
    public void equals_objectsHaveDifferentContent_returnsFalse() {
        final String encoding = "encoding";

        final FileData data1 = new FileData(new byte[] { 0, 12, 34 }, encoding);
        final FileData data2 = new FileData(new byte[] { 1, 2, 3 }, encoding);

        assertThat(data1.equals(data2)).isFalse();
        assertThat(data2.equals(data1)).isFalse();
    }

    @Test
    public void equals_objectsHaveDifferentEncoding_returnsFalse() {
        final byte[] content = new byte[] { 5, 6, 7 };

        final FileData data1 = new FileData(content, null);
        final FileData data2 = new FileData(content, "encoding");

        assertThat(data1.equals(data2)).isFalse();
        assertThat(data2.equals(data1)).isFalse();
    }

    @Test
    public void equals_nullAndOtherClass_returnFalse() {
        final FileData data = new FileData(new byte[0]);

        assertThat(data.equals(new Object())).isFalse();
        assertThat(data.equals(null)).isFalse();
    }

    @Test
    public void toString_encodingIsNull_dataIsEmpty() {
        final FileData data = new FileData(new byte[0]);
        final StringBuilder expected = new StringBuilder();
        expected.append("{\n");
        expected.append("  \"encoding\": null,\n");
        expected.append("  \"content\": []\n");
        expected.append('}');

        final String result = data.toString();

        assertThat(result).isEqualTo(expected.toString());
    }

    @Test
    public void toString_encodingIsNotNull_dataIsNotEmpty() {
        final FileData data = new FileData(new byte[] { 13, 25, 65 }, "UTF-8");
        final StringBuilder expected = new StringBuilder();
        expected.append("{\n");
        expected.append("  \"encoding\": \"UTF-8\",\n");
        expected.append("  \"content\": [13, 25, 65]\n");
        expected.append('}');

        final String result = data.toString();

        assertThat(result).isEqualTo(expected.toString());
    }
}
