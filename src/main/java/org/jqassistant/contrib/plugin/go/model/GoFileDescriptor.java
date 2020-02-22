package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

public interface GoFileDescriptor extends GoDescriptor, FileDescriptor {

    @Relation("DECLARES")
    List<MethodDescriptor> getDeclaredMethods();

}
