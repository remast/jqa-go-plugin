package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

@Relation("DEPENDS_ON")
public interface ModuleDependsOnDescriptor extends Descriptor {

    @Incoming
    ModuleDescriptor getDependency();

    @Outgoing
    ModuleDescriptor getDependent();

}
