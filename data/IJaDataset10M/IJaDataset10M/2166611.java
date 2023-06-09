package bsh.org.objectweb.asm.commons;

import java.util.Collections;
import java.util.Map;

public class SimpleRemapper extends Remapper {

    private final Map mapping;

    public SimpleRemapper(Map mapping) {
        this.mapping = mapping;
    }

    public SimpleRemapper(String oldName, String newName) {
        this.mapping = Collections.singletonMap(oldName, newName);
    }

    public String mapMethodName(String owner, String name, String desc) {
        String s = map(owner + '.' + name + desc);
        return s == null ? name : s;
    }

    public String mapFieldName(String owner, String name, String desc) {
        String s = map(owner + '.' + name);
        return s == null ? name : s;
    }

    public String map(String key) {
        return (String) mapping.get(key);
    }
}
