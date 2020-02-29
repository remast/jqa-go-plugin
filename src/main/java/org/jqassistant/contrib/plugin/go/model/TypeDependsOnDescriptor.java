package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;
import org.jqassistant.contrib.plugin.go.report.Go;

@Go(Go.GoLanguageElement.TypeDepdendency)
@Relation("DEPENDS_ON")
public interface TypeDependsOnDescriptor extends Descriptor {

    @Incoming
    TypeDescriptor getDependency();

    @Outgoing
    TypeDescriptor getDependent();

    Integer getWeight();

    void setWeight(Integer weight);

}
