package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.contrib.plugin.go.report.Go;

import java.util.List;

/**
 * Describes a Go package.
 */
@Go(Go.GoLanguageElement.Package)
@Label(value = "Package")
public interface PackageDescriptor extends GoDescriptor, PackageMemberDescriptor {

    @Relation("CONTAINS")
    List<MethodDescriptor> getMethods();

}
