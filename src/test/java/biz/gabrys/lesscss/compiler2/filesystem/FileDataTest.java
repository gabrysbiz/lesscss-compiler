package biz.gabrys.lesscss.compiler2.filesystem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class FileDataTest {

    @Test(expected = IllegalArgumentException.class)
    public void construct_contentIsNull_throwsException() {
        new FileData(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_contentIsNullAndEncodingIsSet_throwsException() {
        new FileData(null, "encoding");
    }

    @Test
    public void testEqualsAndHashCodeContracts() {
        EqualsVerifier.forClass(FileData.class).usingGetClass().verify();
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
