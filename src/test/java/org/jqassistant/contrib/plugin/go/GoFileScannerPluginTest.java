package org.jqassistant.contrib.plugin.go;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;
import org.hamcrest.CoreMatchers;
import org.jqassistant.contrib.plugin.go.model.GoFileDescriptor;
import org.jqassistant.contrib.plugin.go.model.GoFunctionDescriptor;
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
    public void scanGoFile() {
        store.beginTransaction();
        // Scan the test Go file located as resource in the classpath
        //File testFile = new File(getClassesDirectory(GoFileScannerPluginTest.class), "/example.go");
        File testFile = new File("C:\\projects\\jqa-go-plugin\\src\\test\\resources", "example.go");
        
        // Scan the Go file and assert that the returned descriptor is a CSVFileDescriptor
        assertThat(getScanner().scan(testFile, "/example.go", DefaultScope.NONE), CoreMatchers.<Descriptor>instanceOf(GoFileDescriptor.class));

        // Determine the GoFileDescriptor by executing a Cypher query
        TestResult testResult = query("MATCH (goFile:Go:File) RETURN goFile");
        List<GoFileDescriptor> goFiles = testResult.getColumn("goFile");
        assertThat(goFiles.size(), equalTo(1));

        // Determine the GoFileDescriptor by executing a Cypher query
        testResult = query("MATCH (goFile:Go:File)-[:DECLARES]->(goFunc:Go:Function) RETURN goFunc");
        List<GoFunctionDescriptor> goFuncs = testResult.getColumn("goFunc");
        assertThat(goFuncs.size(), equalTo(4));

        store.commitTransaction();
    }
}
