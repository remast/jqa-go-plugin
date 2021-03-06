package org.jqassistant.contrib.plugin.go.report;

import com.buschmais.jqassistant.core.report.api.SourceProvider;
import com.buschmais.jqassistant.core.report.api.model.Language;
import com.buschmais.jqassistant.core.report.api.model.LanguageElement;
import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import org.jqassistant.contrib.plugin.go.model.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the language elements for "Go".
 */
@Language
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Go {

    GoLanguageElement value();

    enum GoLanguageElement implements LanguageElement {
        Package {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<PackageDescriptor>() {
                    @Override
                    public String getName(PackageDescriptor descriptor) {
                        return descriptor.getFullQualifiedName();
                    }

                    @Override
                    public String getSourceFile(PackageDescriptor descriptor) {
                        return null;
                    }

                    @Override
                    public Integer getLineNumber(PackageDescriptor descriptor) {
                        return null;
                    }
                };
            }
        },
        Type {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<GoFileDescriptor>() {
                    @Override
                    public String getName(GoFileDescriptor descriptor) {
                        return descriptor.getFileName();
                    }

                    @Override
                    public String getSourceFile(GoFileDescriptor descriptor) {
                        return descriptor.getFileName();
                    }

                    @Override
                    public Integer getLineNumber(GoFileDescriptor descriptor) {
                        return null;
                    }
                };
            }
        },
        Field {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new MemberSourceProvider();
            }
        },
        Variable {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<VariableDescriptor>() {

                    @Override
                    public String getName(VariableDescriptor descriptor) {
                        return descriptor.getMethod().getName() + "#" + descriptor.getName(); //descriptor.getMethod().getSignature() + "#" + descriptor.getSignature();
                    }

                    @Override
                    public String getSourceFile(VariableDescriptor descriptor) {
                        TypeDescriptor declaringType = descriptor.getMethod().getDeclaringType();
                        return declaringType instanceof GoFileDescriptor ? ((GoFileDescriptor) declaringType).getFileName() : null;
                    }

                    @Override
                    public Integer getLineNumber(VariableDescriptor descriptor) {
                        return descriptor.getMethod().getFirstLineNumber();
                    }
                };
            }
        },
        ReadField {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<ReadsDescriptor>() {
                    @Override
                    public String getName(ReadsDescriptor descriptor) {
                        return new MemberSourceProvider().getName(descriptor.getMethod()) + ", line " + descriptor.getLineNumber();
                    }

                    @Override
                    public String getSourceFile(ReadsDescriptor descriptor) {
                        TypeDescriptor declaringType = descriptor.getMethod().getDeclaringType();
                        return declaringType instanceof GoFileDescriptor ? ((FileDescriptor) descriptor).getFileName() : null;
                    }

                    @Override
                    public Integer getLineNumber(ReadsDescriptor descriptor) {
                        return descriptor.getLineNumber();
                    }
                };
            }
        },
        WriteField {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<WritesDescriptor>() {
                    @Override
                    public String getName(WritesDescriptor descriptor) {
                        return new MemberSourceProvider().getName(descriptor.getMethod()) + ", line " + descriptor.getLineNumber();
                    }

                    @Override
                    public String getSourceFile(WritesDescriptor descriptor) {
                        TypeDescriptor declaringType = descriptor.getMethod().getDeclaringType();
                        return declaringType instanceof GoFileDescriptor ? ((FileDescriptor) declaringType).getFileName() : null;
                    }

                    @Override
                    public Integer getLineNumber(WritesDescriptor descriptor) {
                        return descriptor.getLineNumber();
                    }
                };
            }
        },
        Method {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new MemberSourceProvider();
            }
        },
        Constructor {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new MemberSourceProvider();
            }
        },
        MethodInvocation {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<InvokesDescriptor>() {
                    @Override
                    public String getName(InvokesDescriptor descriptor) {
                        return new MemberSourceProvider().getName(descriptor.getInvokingMethod()) + ", line " + descriptor.getLineNumber();
                    }

                    @Override
                    public String getSourceFile(InvokesDescriptor descriptor) {
                        TypeDescriptor declaringType = descriptor.getInvokingMethod().getDeclaringType();
                        return declaringType instanceof GoFileDescriptor ? ((FileDescriptor) declaringType).getFileName() : null;
                    }

                    @Override
                    public Integer getLineNumber(InvokesDescriptor descriptor) {
                        return descriptor.getLineNumber();
                    }
                };
            }
        },
        TypeDepdendency {
            @Override
            public SourceProvider<? extends Descriptor> getSourceProvider() {
                return new SourceProvider<TypeDependsOnDescriptor>() {
                    @Override
                    public String getName(TypeDependsOnDescriptor descriptor) {
                        return descriptor.getDependent().getName() + "->" + descriptor.getDependency().getName();
                    }

                    @Override
                    public String getSourceFile(TypeDependsOnDescriptor descriptor) {
                        TypeDescriptor dependent = descriptor.getDependent();
                        return dependent instanceof GoFileDescriptor ? ((FileDescriptor) dependent).getFileName() : null;
                    }

                    @Override
                    public Integer getLineNumber(TypeDependsOnDescriptor descriptor) {
                        return 0;
                    }
                };
            }
        };

        @Override
        public String getLanguage() {
            return "Go";
        }

        /**
         * {@link SourceProvider}
         * implementation for type members.
         */
        private static class MemberSourceProvider implements SourceProvider<MemberDescriptor> {
            @Override
            public String getName(MemberDescriptor descriptor) {
                return descriptor.getName();
            }

            @Override
            public String getSourceFile(MemberDescriptor descriptor) {
                TypeDescriptor declaringType = descriptor.getDeclaringType();
                return declaringType instanceof GoFileDescriptor ? ((FileDescriptor) declaringType).getFileName() : null;
            }

            @Override
            public Integer getLineNumber(MemberDescriptor descriptor) {
                return null;
            }
        }

        /**
         * Returns the file name of the given descriptor if it extends
         * {@link FileDescriptor} .
         *
         * @param descriptor
         *            The descriptor.
         * @return The
         */
        private static String getFileName(Descriptor descriptor) {
            return descriptor instanceof FileDescriptor ? ((FileDescriptor) descriptor).getFileName() : null;
        }
    }
}
