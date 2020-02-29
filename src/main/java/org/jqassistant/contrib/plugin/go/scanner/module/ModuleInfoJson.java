package org.jqassistant.contrib.plugin.go.scanner.module;

import com.google.gson.annotations.SerializedName;

public class ModuleInfoJson {

    @SerializedName("Path")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
