package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Function")
public interface GoFunctionDescriptor extends GoDescriptor, NamedDescriptor {

    String getName();
    void setName(String name);

    Integer getFirstLineNumber();
    void setFirstLineNumber(Integer firstLineNumber);

    Integer getLastLineNumber();
    void setLastLineNumber(Integer lastLineNumber);

    int getEffectiveLineCount();
    void setEffectiveLineCount(int effectiveLineCount);
}
