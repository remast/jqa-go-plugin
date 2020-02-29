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
import org.jqassistant.contrib.plugin.go.scanner.module.ModuleJson;

@Requires(FileDescriptor.class)
public class ModuleFileScannerPlugin extends AbstractScannerPlugin<FileResource, GoFileDescriptor> {

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

        String o = execToString(item.getFile().getParentFile(), "go mod edit -json");
        Gson gson = new Gson();
        ModuleJson moduleJson = gson.fromJson(o, ModuleJson.class);

        return goFileDescriptor;
    }

    public String execToString(File workingDirectory, String command) throws IOException {
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
