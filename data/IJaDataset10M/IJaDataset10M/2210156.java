package com.jme3.material;

import com.jme3.asset.AssetManager;
import com.jme3.export.*;
import com.jme3.shader.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a technique instance.
 */
public class Technique implements Savable {

    private static final Logger logger = Logger.getLogger(Technique.class.getName());

    private TechniqueDef def;

    private Material owner;

    private ArrayList<Uniform> worldBindUniforms;

    private DefineList defines;

    private Shader shader;

    private boolean needReload = true;

    /**
     * Creates a new technique instance that implements the given
     * technique definition.
     * 
     * @param owner The material that will own this technique
     * @param def The technique definition being implemented.
     */
    public Technique(Material owner, TechniqueDef def) {
        this.owner = owner;
        this.def = def;
        if (def.isUsingShaders()) {
            this.worldBindUniforms = new ArrayList<Uniform>();
            this.defines = new DefineList();
        }
    }

    /**
     * Serialization only. Do not use.
     */
    public Technique() {
    }

    /**
     * Returns the technique definition that is implemented by this technique
     * instance. 
     * 
     * @return the technique definition that is implemented by this technique
     * instance. 
     */
    public TechniqueDef getDef() {
        return def;
    }

    /**
     * Returns the shader currently used by this technique instance.
     * <p>
     * Shaders are typically loaded dynamically when the technique is first
     * used, therefore, this variable will most likely be null most of the time.
     * 
     * @return the shader currently used by this technique instance.
     */
    public Shader getShader() {
        return shader;
    }

    /**
     * Returns a list of uniforms that implements the world parameters
     * that were requested by the material definition.
     * 
     * @return a list of uniforms implementing the world parameters.
     */
    public List<Uniform> getWorldBindUniforms() {
        return worldBindUniforms;
    }

    /**
     * Called by the material to tell the technique a parameter was modified
     */
    void notifySetParam(String paramName, VarType type, Object value) {
        String defineName = def.getShaderParamDefine(paramName);
        if (defineName != null) {
            needReload = defines.set(defineName, type, value);
        }
        if (shader != null) {
            updateUniformParam(paramName, type, value);
        }
    }

    /**
     * Called by the material to tell the technique a parameter was cleared
     */
    void notifyClearParam(String paramName) {
        String defineName = def.getShaderParamDefine(paramName);
        if (defineName != null) {
            needReload = defines.remove(defineName);
        }
        if (shader != null) {
            if (!paramName.startsWith("m_")) {
                paramName = "m_" + paramName;
            }
            shader.removeUniform(paramName);
        }
    }

    void updateUniformParam(String paramName, VarType type, Object value, boolean ifNotOwner) {
        Uniform u = shader.getUniform(paramName);
        switch(type) {
            case Texture2D:
            case Texture3D:
            case TextureArray:
            case TextureCubeMap:
            case Int:
                u.setValue(VarType.Int, value);
                break;
            default:
                u.setValue(type, value);
                break;
        }
    }

    void updateUniformParam(String paramName, VarType type, Object value) {
        updateUniformParam(paramName, type, value, false);
    }

    /**
     * Returns true if the technique must be reloaded.
     * <p>
     * If a technique needs to reload, then the {@link Material} should
     * call {@link #makeCurrent(com.jme3.asset.AssetManager) } on this
     * technique.
     * 
     * @return true if the technique must be reloaded.
     */
    public boolean isNeedReload() {
        return needReload;
    }

    /**
     * Prepares the technique for use by loading the shader and setting
     * the proper defines based on material parameters.
     * 
     * @param assetManager The asset manager to use for loading shaders.
     */
    public void makeCurrent(AssetManager assetManager) {
        if (def.isUsingShaders()) {
            DefineList newDefines = new DefineList();
            Collection<MatParam> params = owner.getParams();
            for (MatParam param : params) {
                String defineName = def.getShaderParamDefine(param.getName());
                if (defineName != null) {
                    newDefines.set(defineName, param.getVarType(), param.getValue());
                }
            }
            if (!needReload && defines.getCompiled().equals(newDefines.getCompiled())) {
                newDefines = null;
            } else {
                defines.clear();
                defines.addFrom(newDefines);
                loadShader(assetManager);
            }
        }
    }

    private void loadShader(AssetManager manager) {
        DefineList allDefines = new DefineList();
        allDefines.addFrom(def.getShaderPresetDefines());
        allDefines.addFrom(defines);
        ShaderKey key = new ShaderKey(def.getVertexShaderName(), def.getFragmentShaderName(), allDefines, def.getShaderLanguage());
        shader = manager.loadShader(key);
        if (shader == null) {
            logger.warning("Failed to reload shader!");
            return;
        }
        worldBindUniforms.clear();
        if (def.getWorldBindings() != null) {
            for (UniformBinding binding : def.getWorldBindings()) {
                Uniform uniform = shader.getUniform("g_" + binding.name());
                uniform.setBinding(binding);
                if (uniform != null) {
                    worldBindUniforms.add(uniform);
                }
            }
        }
        needReload = false;
    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(def, "def", null);
        oc.writeSavableArrayList(worldBindUniforms, "worldBindUniforms", null);
        oc.write(defines, "defines", null);
        oc.write(shader, "shader", null);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        def = (TechniqueDef) ic.readSavable("def", null);
        worldBindUniforms = ic.readSavableArrayList("worldBindUniforms", null);
        defines = (DefineList) ic.readSavable("defines", null);
        shader = (Shader) ic.readSavable("shader", null);
    }
}
