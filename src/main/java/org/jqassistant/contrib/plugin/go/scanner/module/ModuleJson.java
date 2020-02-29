package org.jqassistant.contrib.plugin.go.scanner.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModuleJson {

    @SerializedName("Go")
    private String go;

    @SerializedName("Module")
    private ModuleInfoJson module;

    @SerializedName("Require")
    private List<RequiredModuleJson> require;

    public List<RequiredModuleJson> getRequire() {
        return require;
    }

    public void setRequire(List<RequiredModuleJson> require) {
        this.require = require;
    }

    public ModuleInfoJson getModule() {
        return module;
    }

    public void setModule(ModuleInfoJson module) {
        this.module = module;
    }

    public String getGo() {
        return go;
    }

    public void setGo(String go) {
        this.go = go;
    }
}
