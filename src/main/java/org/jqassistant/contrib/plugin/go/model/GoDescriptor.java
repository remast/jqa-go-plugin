package org.jqassistant.contrib.plugin.go.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

/**
 * Defines the label which is shared by all nodes representing PLSQL structures.
 * @author Jonathan
 *
 */

@Abstract
@Label("Go")
public interface GoDescriptor extends Descriptor {
	
}
