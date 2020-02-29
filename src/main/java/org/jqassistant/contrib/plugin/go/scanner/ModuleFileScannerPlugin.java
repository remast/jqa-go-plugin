package org.jqassistant.contrib.plugin.go.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.google.gson.Gson;
import org.jqassistant.contrib.plugin.go.model.GoFileDescriptor;
import java.io.ByteArrayOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.*;

import org.jqassistant.contrib.plugin.go.model.ModuleDependsOnDescriptor;
import org.jqassistant.contrib.plugin.go.model.ModuleDescriptor;
import org.jqassistant.contrib.plugin.go.scanner.module.ModuleJson;
import org.jqassistant.contrib.plugin.go.scanner.module.RequiredModuleJson;

@Requires(FileDescriptor.class)
public class ModuleFileScannerPlugin extends AbstractScannerPlugin<FileResource, GoFileDescriptor> {

    private static final String COMMAND_GO_MOD_JSON = "go mod edit -json";

    private Gson gson;

    @Override
    public void initialize() {
        gson = new Gson();
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        return item.getFile().getName().equals("go.mod");
    }

    @Override
    public GoFileDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        ScannerContext context = scanner.getContext();
        final Store store = context.getStore();
        final FileDescriptor fileDescriptor = context.getCurrentDescriptor();
        final GoFileDescriptor goFileDescriptor = store.addDescriptorType(fileDescriptor, GoFileDescriptor.class);

        final ModuleJson moduleJson = readGoMod(item.getFile().getParentFile());

        final ModuleDescriptor rootModuleDescriptor = store.create(ModuleDescriptor.class);
        rootModuleDescriptor.setFullQualifiedName(moduleJson.getModule().getPath());
        for (RequiredModuleJson requiredModuleJson : moduleJson.getRequire()) {
            final ModuleDescriptor dependencyModuleDescriptor = store.create(ModuleDescriptor.class);
            dependencyModuleDescriptor.setVersion(requiredModuleJson.getVersion());
            dependencyModuleDescriptor.setFullQualifiedName(requiredModuleJson.getPath());
            ModuleDependsOnDescriptor dependsOnDescriptor = store.create(rootModuleDescriptor, ModuleDependsOnDescriptor.class, dependencyModuleDescriptor);
        }

        return goFileDescriptor;
    }

    private ModuleJson readGoMod(File workingDirectory) throws IOException {
        String goModJson = execeuteCommandWithOutput(workingDirectory, COMMAND_GO_MOD_JSON);
        return gson.fromJson(goModJson, ModuleJson.class);
    }

    private String execeuteCommandWithOutput(File workingDirectory, String command) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommandLine commandline = CommandLine.parse(command);
        DefaultExecutor exec = new DefaultExecutor();
        exec.setWorkingDirectory(workingDirectory);
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        exec.setStreamHandler(streamHandler);
        exec.execute(commandline);
        return outputStream.toString();
    }

}
