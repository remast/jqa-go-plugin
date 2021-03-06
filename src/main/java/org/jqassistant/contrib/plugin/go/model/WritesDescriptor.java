package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.contrib.plugin.go.report.Go;

import static com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import static com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

/**
 * Defines a WRITES relation between a method and a field.
 */
@Relation("WRITES")
@Go(Go.GoLanguageElement.WriteField)
public interface WritesDescriptor extends Descriptor, LineNumberDescriptor {

    @Outgoing
    MethodDescriptor getMethod();

    @Incoming
    FieldDescriptor getField();

}
