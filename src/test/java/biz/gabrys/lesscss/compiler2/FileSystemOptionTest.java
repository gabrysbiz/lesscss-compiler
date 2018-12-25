package biz.gabrys.lesscss.compiler2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

import biz.gabrys.lesscss.compiler2.filesystem.FileSystem;

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
    public void hashCode_optionsAreTheSame_returnsTheSameNumber() {
        final String className = "className";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");

        final FileSystemOption option1 = new FileSystemOption(className, parameters);
        final FileSystemOption option2 = new FileSystemOption(className, parameters);

        final int hashCode1 = option1.hashCode();
        final int hashCode2 = option2.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    public void hashCode_optionsHaveDifferentClassNames_returnsDifferentNumbers() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");

        final FileSystemOption option1 = new FileSystemOption("className1", parameters);
        final FileSystemOption option2 = new FileSystemOption("className2", parameters);

        final int hashCode1 = option1.hashCode();
        final int hashCode2 = option2.hashCode();

        assertThat(hashCode1).isNotEqualTo(hashCode2);
    }

    @Test
    public void hashCode_optionsHaveDifferentParameters_returnsDifferentNumbers() {
        final String className = "className";
        final Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("name", "value");
        final Map<String, String> parameters2 = new HashMap<>();
        parameters1.put("value", "name");

        final FileSystemOption option1 = new FileSystemOption(className, parameters1);
        final FileSystemOption option2 = new FileSystemOption(className, parameters2);

        final int hashCode1 = option1.hashCode();
        final int hashCode2 = option2.hashCode();

        assertThat(hashCode1).isNotEqualTo(hashCode2);
    }

    @Test
    public void hashCode_optionsAreTheSame_returnsTrue() {
        final String className = "className";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");

        final FileSystemOption option1 = new FileSystemOption(className, parameters);
        final FileSystemOption option2 = new FileSystemOption(className, parameters);

        assertThat(option1.equals(option1)).isTrue();
        assertThat(option1.equals(option2)).isTrue();
        assertThat(option2.equals(option1)).isTrue();
    }

    @Test
    public void equals_optionsHaveDifferentClassNames_returnsFalse() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");

        final FileSystemOption option1 = new FileSystemOption("className1", parameters);
        final FileSystemOption option2 = new FileSystemOption("className2", parameters);

        assertThat(option1.equals(option2)).isFalse();
        assertThat(option2.equals(option1)).isFalse();
    }

    @Test
    public void equals_optionsHaveDifferentParameters_returnFalse() {
        final String className = "className";
        final Map<String, String> parameters1 = new HashMap<>();
        parameters1.put("name", "value");
        final Map<String, String> parameters2 = new HashMap<>();
        parameters1.put("value", "name");

        final FileSystemOption option1 = new FileSystemOption(className, parameters1);
        final FileSystemOption option2 = new FileSystemOption(className, parameters2);

        assertThat(option1.equals(option2)).isFalse();
        assertThat(option2.equals(option1)).isFalse();
    }

    @Test
    public void equals_nullAndOtherClass_returnFalse() {
        final FileSystemOption option = new FileSystemOption("className");

        assertThat(option.equals(new Object())).isFalse();
        assertThat(option.equals(null)).isFalse();
    }

    @Test
    public void toString_parametersIsEmpty() {
        final String className = "org.example.ClassName";
        final FileSystemOption option = new FileSystemOption(className);

        final String result = option.toString();

        assertThat(result).isEqualTo(className);
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
