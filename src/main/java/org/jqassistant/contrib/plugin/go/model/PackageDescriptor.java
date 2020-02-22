package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

/**
 * Describes a Go package.
 */
@Label(value = "Package")
public interface PackageDescriptor extends GoDescriptor, PackageMemberDescriptor, NamedDescriptor {

    @Relation("CONTAINS")
    List<MethodDescriptor> getMethods();

}
