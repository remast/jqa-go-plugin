package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * Interface for value descriptors which provide a type information.
 */
public interface TypedDescriptor {

    @Relation("OF_TYPE")
    TypeDescriptor getType();

    void setType(TypeDescriptor type);
}
