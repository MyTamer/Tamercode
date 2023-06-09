package com.sun.syndication.io.impl;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleGenerator;
import org.jdom.Element;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class ModuleGenerators extends PluginManager {

    private Set _allNamespaces;

    public ModuleGenerators(String propertyKey, BaseWireFeedGenerator parentGenerator) {
        super(propertyKey, null, parentGenerator);
    }

    public ModuleGenerator getGenerator(String uri) {
        return (ModuleGenerator) getPlugin(uri);
    }

    protected String getKey(Object obj) {
        return ((ModuleGenerator) obj).getNamespaceUri();
    }

    public List getModuleNamespaces() {
        return getKeys();
    }

    public void generateModules(List modules, Element element) {
        Map generators = getPluginMap();
        for (int i = 0; i < modules.size(); i++) {
            Module module = (Module) modules.get(i);
            String namespaceUri = module.getUri();
            ModuleGenerator generator = (ModuleGenerator) generators.get(namespaceUri);
            if (generator != null) {
                generator.generate(module, element);
            }
        }
    }

    public Set getAllNamespaces() {
        if (_allNamespaces == null) {
            _allNamespaces = new HashSet();
            List mUris = getModuleNamespaces();
            for (int i = 0; i < mUris.size(); i++) {
                ModuleGenerator mGen = getGenerator((String) mUris.get(i));
                _allNamespaces.addAll(mGen.getNamespaces());
            }
        }
        return _allNamespaces;
    }
}
