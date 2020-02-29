package org.jqassistant.contrib.plugin.go.scanner;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
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
import org.jqassistant.contrib.plugin.go.model.MethodDescriptor;
import org.jqassistant.contrib.plugin.go.model.PackageDescriptor;
import org.jqassistant.contrib.plugin.go.model.ParameterDescriptor;

@Requires(FileDescriptor.class)

public class SourceFileScannerPlugin extends AbstractScannerPlugin<FileResource, GoFileDescriptor> {

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
            InputStream inputStream = item.createStream();
            Lexer lexer = new GoLexer(CharStreams.fromStream(inputStream));
            TokenStream tokenStream = new CommonTokenStream(lexer);
            GoParser parser = new GoParser(tokenStream);

            GoParser.SourceFileContext sourceFileContext = parser.sourceFile();

            GoParser.PackageClauseContext packageClauseContext = sourceFileContext.packageClause();
            PackageDescriptor packageDescriptor = store.create(PackageDescriptor.class);
            packageDescriptor.setName(packageClauseContext.IDENTIFIER().getText());
            // TODO: Add Go Package support

            for (GoParser.FunctionDeclContext functionDeclContext : sourceFileContext.functionDecl()) {
                String name = functionDeclContext.IDENTIFIER().getText();
                int firstLineNumber = functionDeclContext.getStart().getLine();
                int lastLineNumber = functionDeclContext.getStop().getLine();
                int effectiveLineCount = lastLineNumber - firstLineNumber;

                MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
                methodDescriptor.setName(name);
                methodDescriptor.setFirstLineNumber(firstLineNumber);
                methodDescriptor.setLastLineNumber(lastLineNumber);
                methodDescriptor.setEffectiveLineCount(effectiveLineCount);
                methodDescriptor.setExported(GoLanguageHelpers.isExported(name));

                GoParser.SignatureContext signatureContext = functionDeclContext.signature();
                GoParser.ParametersContext parametersContext = signatureContext.parameters();

                for (GoParser.ParameterDeclContext parameterDeclContext : parametersContext.parameterDecl()) {
                    ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);
                    parameterDescriptor.setIndex(parameterDeclContext.getAltNumber());
                    methodDescriptor.getParameters().add(parameterDescriptor);

                    GoParser.Type_Context typeContext = parameterDeclContext.type_();
                    if (typeContext.typeName() != null) {
                        String typeName = typeContext.typeName().IDENTIFIER().getText();
                    } else if (typeContext.typeLit() != null) {
                        GoParser.TypeLitContext typeLitContext = typeContext.typeLit();
                        if (typeLitContext.channelType() != null) {
                            GoParser.ChannelTypeContext channelTypeContext = typeLitContext.channelType();
                            // channelTypeContext.elementType().type_()
                        } else if (typeLitContext.structType() != null) {
                            for (GoParser.FieldDeclContext fieldDeclContext : typeLitContext.structType().fieldDecl()) {
                                GoParser.Type_Context fieldTypeContext = fieldDeclContext.type_();
                            }
                        }
                    }

                    GoParser.IdentifierListContext identifierListContext = parameterDeclContext.identifierList();
                }

                goFileDescriptor.getDeclaredMethods().add(methodDescriptor);
                packageDescriptor.getMethods().add(methodDescriptor);
            }

            for (GoParser.MethodDeclContext methodDeclContext : sourceFileContext.methodDecl()) {
                String name = methodDeclContext.IDENTIFIER().getText();
                int firstLineNumber = methodDeclContext.getStart().getLine();
                int lastLineNumber = methodDeclContext.getStop().getLine();
                int effectiveLineCount = lastLineNumber - firstLineNumber;

                MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
                methodDescriptor.setName(name);
                methodDescriptor.setFirstLineNumber(firstLineNumber);
                methodDescriptor.setLastLineNumber(lastLineNumber);
                methodDescriptor.setEffectiveLineCount(effectiveLineCount);
                methodDescriptor.setExported(GoLanguageHelpers.isExported(name));

                goFileDescriptor.getDeclaredMethods().add(methodDescriptor);
                packageDescriptor.getMethods().add(methodDescriptor);
            }

            for (GoParser.ImportDeclContext importDeclContext : sourceFileContext.importDecl()) {
                for (GoParser.ImportSpecContext importSpecContext : importDeclContext.importSpec()) {
                    String importPath = importSpecContext.importPath().string_().getText();
                    PackageDescriptor importedPackageDescriptor = store.create(PackageDescriptor.class);
                    // TODO: Set name
                    importedPackageDescriptor.setFullQualifiedName(importPath);
                }
            }

            for (GoParser.DeclarationContext declarationContext : sourceFileContext.declaration()) {
                for (GoParser.ConstSpecContext constSpecContext : declarationContext.constDecl().constSpec()) {
                    GoParser.ExpressionListContext expressionListContext = constSpecContext.expressionList();
                    for (GoParser.ExpressionContext expressionContext : expressionListContext.expression()) {
                    }
                }
            }

            return goFileDescriptor;
        }
    }

}
