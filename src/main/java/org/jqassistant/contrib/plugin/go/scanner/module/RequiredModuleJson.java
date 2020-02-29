package org.jqassistant.contrib.plugin.go.scanner.module;

import com.google.gson.annotations.SerializedName;

public class RequiredModuleJson {

    @SerializedName("Path")
    private String path;

    @SerializedName("Version")
    private String version;

    @SerializedName("Indirect")
    private Boolean indirect;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIndirect() {
        return indirect;
    }

    public void setIndirect(Boolean indirect) {
        this.indirect = indirect;
    }
}
