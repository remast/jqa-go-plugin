package org.jqassistant.contrib.plugin.go.scanner;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jqassistant.contrib.plugin.go.antlr.GoLexer;
import org.jqassistant.contrib.plugin.go.antlr.GoParser;
import org.jqassistant.contrib.plugin.go.model.GoFileDescriptor;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;

@Requires(FileDescriptor.class)

public class GoFileScannerPlugin extends AbstractScannerPlugin<FileResource, GoFileDescriptor> {

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        return path.toLowerCase().endsWith(".go");
    }

    @Override
    public GoFileDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        ScannerContext context = scanner.getContext();
        final Store store = context.getStore();
        // Open the input stream for reading the file.
        try (InputStream stream = item.createStream()) {
            // Retrieve the scanned file node from the scanner context.
            final FileDescriptor fileDescriptor = context.getCurrentDescriptor();

            // Add the Go label.
            final GoFileDescriptor goFileDescriptor = store.addDescriptorType(fileDescriptor, GoFileDescriptor.class);

            // Parse the stream using ANTLR Parser
            try {
                //ANTLR4 PLSQL Lexer & Parser
                InputStream inputStream = item.createStream();
                Lexer lexer = new GoLexer(CharStreams.fromStream(inputStream));
                TokenStream tokenStream = new CommonTokenStream(lexer);
                GoParser parser = new GoParser(tokenStream);

                GoParser.SourceFileContext sourceFileContext = parser.sourceFile();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }


            return goFileDescriptor;
        }
    }

}