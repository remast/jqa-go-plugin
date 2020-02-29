package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import org.jqassistant.contrib.plugin.go.report.Go;

import java.util.List;

/**
 * Describes a field (i.e. static or instance variable) of a Java class.
 */
@Go(Go.GoLanguageElement.Field)
@Label(value = "Field")
public interface FieldDescriptor extends MemberDescriptor, TypedDescriptor, AccessModifierDescriptor {

    List<WritesDescriptor> getWrittenBy();

    List<ReadsDescriptor> getReadBy();
}
