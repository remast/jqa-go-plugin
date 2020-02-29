package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label(value = "Module", usingIndexedPropertyOf = FullQualifiedNameDescriptor.class)
public interface ModuleDescriptor extends FullQualifiedNameDescriptor, NamedDescriptor {

    @Property("version")
    String getVersion();
    void setVersion(String version);

    @Relation.Outgoing
    List<ModuleDependsOnDescriptor> getDependencies();

    @Relation.Incoming
    List<ModuleDependsOnDescriptor> getDependents();

}
