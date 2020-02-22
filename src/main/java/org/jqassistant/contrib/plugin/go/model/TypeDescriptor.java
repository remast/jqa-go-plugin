package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

import java.util.List;
import java.util.Set;

import static com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

/**
 * Describes a Java type.
 */
@Label(value = "Type", usingIndexedPropertyOf = FullQualifiedNameDescriptor.class)
public interface TypeDescriptor extends GoDescriptor, PackageMemberDescriptor {

    /**
     * Return the declared methods.
     *
     * @return The declared methods.
     */
    @Outgoing
    @Relation("DECLARES")
    List<MethodDescriptor> getDeclaredMethods();

}
