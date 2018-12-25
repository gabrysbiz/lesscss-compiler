package biz.gabrys.lesscss.compiler2.io;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public final class FilenameUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void getName_pathIsNull_throwsException() {
        FilenameUtils.getName(null);
    }

    @Test
    public void getName_pathContainsWindowsSeparators_returnsName() {
        final String name = FilenameUtils.getName("C:\\directory\\windows.txt");
        assertThat(name).isEqualTo("windows.txt");
    }

    @Test
    public void getName_pathContainsUnixSeparators_returnsName() {
        final String name = FilenameUtils.getName("/directory/unix.rules");
        assertThat(name).isEqualTo("unix.rules");
    }

    @Test
    public void getName_pathContainsWindowsAndUnixSeparators_windowsLast_returnsName() {
        final String name = FilenameUtils.getName("/directory\\unix.mixed");
        assertThat(name).isEqualTo("unix.mixed");
    }

    @Test
    public void getName_pathContainsWindowsAndUnixSeparators_unixLast_returnsName() {
        final String name = FilenameUtils.getName("C:\\directory/windows.mixed");
        assertThat(name).isEqualTo("windows.mixed");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getExtension_pathIsNull_throwsException() {
        FilenameUtils.getExtension(null);
    }

    @Test
    public void getExtension_fileHasExtenion_returnsExtension() {
        final String extension = FilenameUtils.getExtension("/system/file.txt");
        assertThat(extension).isEqualTo("txt");
    }

    @Test
    public void getExtension_fileDoesNotHaveExtenion_returnsEmptyText() {
        final String extension = FilenameUtils.getExtension("C:\\direc.tory\\file");
        assertThat(extension).isEmpty();
    }
}
