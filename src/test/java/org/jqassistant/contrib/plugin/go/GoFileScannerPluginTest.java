package org.jqassistant.contrib.plugin.go;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import org.hamcrest.CoreMatchers;
import org.jqassistant.contrib.plugin.go.model.GoFileDescriptor;
import org.jqassistant.contrib.plugin.go.model.MethodDescriptor;
import org.jqassistant.contrib.plugin.go.model.PackageDescriptor;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.util.List;

public class GoFileScannerPluginTest extends AbstractPluginIT {

    /**
     * TRICKY: For JUnit Jupiter we need to override the standard method to get the classes directory.
     * @see <a href="https://www.baeldung.com/junit-src-test-resources-directory-path">Get the Path of the /src/test/resources Directory in JUnit</a>"
     */
    protected File getClassesDirectory(Class<?> rootClass) {
        String resourceName = ".";
        ClassLoader classLoader = rootClass.getClassLoader();
        File directory = new File(classLoader.getResource(resourceName).getFile());
        return directory;
    }

    @Test
    public void scanExampleGo() {
        store.beginTransaction();

        File testFile = new File(getClassesDirectory(GoFileScannerPluginTest.class), "example.go");

        assertThat(getScanner().scan(testFile, "example.go", DefaultScope.NONE), CoreMatchers.<Descriptor>instanceOf(GoFileDescriptor.class));

        // Check files
        TestResult testResult = query("MATCH (file:Go:File) RETURN file");
        List<GoFileDescriptor> files = testResult.getColumn("file");
        assertThat(files.size(), equalTo(1));
        GoFileDescriptor file = files.get(0);
        assertThat(file.getFileName(), equalTo("example.go"));

        // Check package
        testResult = query("MATCH (package:Go:Package{name: 'main'}) RETURN package");
        List<PackageDescriptor> packages = testResult.getColumn("package");
        assertThat(packages.size(), equalTo(1));
        PackageDescriptor goPackage = packages.get(0);
        assertThat(goPackage.getName(), equalTo("main"));

        // Check functions
        testResult = query("MATCH (:Go:File)-[:DECLARES]->(goFunction:Go:Method) RETURN goFunction");
        List<MethodDescriptor> functions = testResult.getColumn("goFunction");
        assertThat(functions.size(), equalTo(4));

        MethodDescriptor sieveFunction = functions.get(2);

        store.commitTransaction();
    }
}
