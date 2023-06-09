package suneido.language.builtin;

import suneido.SuContainer;
import suneido.language.Args;
import suneido.language.BuiltinClass;

public class ObjectClass extends BuiltinClass {

    @Override
    public Object call(Object... args) {
        return newInstance(args);
    }

    @Override
    public Object newInstance(Object[] args) {
        return create(args);
    }

    /** used by direct calls in generated code */
    public static Object create(Object... args) {
        return Args.collectArgs(new SuContainer(), args);
    }
}
