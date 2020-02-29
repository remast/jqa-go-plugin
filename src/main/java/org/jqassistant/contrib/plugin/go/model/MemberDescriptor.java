package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import static com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;

/**
 * Defines a member of Java type.
 */
@Label("Member")
@Abstract
public interface MemberDescriptor extends GoDescriptor, NamedDescriptor, AccessModifierDescriptor, Descriptor {

    /**
     * Return the declaring type.
     *
     * @return The declaring type.
     */
    @Incoming
    @Relation("DECLARES")
    TypeDescriptor getDeclaringType();

}
