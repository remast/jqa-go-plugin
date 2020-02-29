package org.jqassistant.contrib.plugin.go;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import org.hamcrest.CoreMatchers;
import org.jqassistant.contrib.plugin.go.model.GoFileDescriptor;
import org.jqassistant.contrib.plugin.go.model.MethodDescriptor;
import org.jqassistant.contrib.plugin.go.model.PackageDescriptor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ModuleFileScannerPluginIT extends AbstractPluginIT {

    /**
     * TRICKY: For JUnit Jupiter we need to override the standard method to get the classes directory.
     * @see <a href="https://www.baeldung.com/junit-src-test-resources-directory-path">Get the Path of the /src/test/resources Directory in JUnit</a>"
     */
    protected File getClassesDirectory(Class<?> rootClass) {
        String resourceName = ".";
        ClassLoader classLoader = rootClass.getClassLoader();
        return new File(classLoader.getResource(resourceName).getFile());
    }

    @Test
    public void scanExampleGo() {
        store.beginTransaction();

        File testFile = new File(getClassesDirectory(ModuleFileScannerPluginIT.class), "mymod/go.mod");

        assertThat(getScanner().scan(testFile, "go.mod", DefaultScope.NONE), CoreMatchers.<Descriptor>instanceOf(GoFileDescriptor.class));

        // Check files
        TestResult testResult = query("MATCH (file:Go:File) RETURN file");
        List<GoFileDescriptor> files = testResult.getColumn("file");
        assertThat(files.size(), equalTo(1));
        GoFileDescriptor file = files.get(0);
        assertThat(file.getFileName(), equalTo("go.mod"));


        store.commitTransaction();
    }
}
