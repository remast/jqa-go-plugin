package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.contrib.plugin.go.report.Go;

import java.util.List;

/**
 * Describes a Go method of function.
 */
@Go(Go.GoLanguageElement.Method)
@Label(value = "Method")
public interface MethodDescriptor extends MemberDescriptor, NamedDescriptor, AccessModifierDescriptor {

    /**
     * Return all declared parameters of this method.
     *
     * @return The declared parameters.
     */
    @Relation("HAS")
    List<ParameterDescriptor> getParameters();

    @Property("firstLineNumber")
    Integer getFirstLineNumber();
    void setFirstLineNumber(Integer firstLineNumber);

    @Property("lastLineNumber")
    Integer getLastLineNumber();
    void setLastLineNumber(Integer lastLineNumber);

    @Property("effectiveLineCount")
    int getEffectiveLineCount();
    void setEffectiveLineCount(int effectiveLineCount);
}
