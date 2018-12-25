package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;
import nl.jqno.equalsverifier.EqualsVerifier;

public class FileSystemOptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void construct_classIsNull_throwsException() {
        new FileSystemOption((Class<FileSystem>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_clazzIsNotNullAndParametersAreNull_throwsException() {
        new FileSystemOption(FileSystem.class, null);
    }

    @Test
    public void construct_classAndParametersAreValid_createdSuccessfully() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");

        final FileSystemOption option = new FileSystemOption(FileSystem.class, parameters);

        assertThat(option.getClassName()).isEqualTo(FileSystem.class.getName());
        assertThat(option.getParameters()).containsExactly(MapEntry.entry("name", "value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_classNameIsNull_throwsException() {
        new FileSystemOption((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_classNameIsBlank_throwsException() {
        new FileSystemOption(" \r\t  ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_classNameIsNotBlankAndParametersAreNull_throwsException() {
        new FileSystemOption("className", null);
    }

    @Test
    public void construct_classNameAndParametersAreValid_createdSuccessfully() {
        final String className = "className";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");

        final FileSystemOption option = new FileSystemOption(className, parameters);

        assertThat(option.getClassName()).isSameAs(className);
        assertThat(option.getParameters()).containsExactly(MapEntry.entry("name", "value"));
    }

    @Test
    public void getParameters_mapIsOrdered_orderIsPreserved() {
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("b", "v_a,l=B");
        parameters.put("a", "v,a_l=A");
        parameters.put("c", "v=a,l_C");
        final FileSystemOption option = new FileSystemOption("className", parameters);

        final Map<String, String> result = option.getParameters();

        assertThat(result).containsExactly(MapEntry.entry("b", "v_a,l=B"), MapEntry.entry("a", "v,a_l=A"), MapEntry.entry("c", "v=a,l_C"));
    }

    @Test
    public void testEqualsAndHashCodeContracts() {
        EqualsVerifier.forClass(FileSystemOption.class).usingGetClass().withNonnullFields("className", "parameters").verify();
    }

    @Test
    public void toString_parametersIsNotEmpty() {
        final String className = "org.example.ClassName";
        final Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("name1", "value1");
        parameters.put("n,a_m=e", "v_al_=u,e");
        parameters.put("null", null);
        parameters.put("name2", "val__=,,ue2");
        final FileSystemOption option = new FileSystemOption(className, parameters);

        final String result = option.toString();

        assertThat(result).isEqualTo("org.example.ClassName,name1=value1,n_,a__m_=e=v__al___=u_,e,null=,name2=val_____=_,_,ue2");
    }
}
