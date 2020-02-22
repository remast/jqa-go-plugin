package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.xo.neo4j.api.annotation.Property;


public interface AccessModifierDescriptor {

    @Property("exported")
    Boolean isExported();
    void setExported(Boolean f);
}
