package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import org.jqassistant.contrib.plugin.go.report.Go;

/**
 * Describes a field (i.e. static or instance variable) of a Java class.
 */
@Go(Go.GoLanguageElement.Field)
@Label(value = "Variable")
public interface VariableDescriptor extends GoDescriptor, NamedDescriptor, TypedDescriptor {

    @Relation("DECLARES")
    @Incoming
    MethodDescriptor getMethod();
}
