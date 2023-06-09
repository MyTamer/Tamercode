package com.android.dx.ssa;

import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.util.ToHuman;

/**
 * Represents a mapping between two register numbering schemes. 
 * Subclasses of this may be mutable, and as such the mapping provided
 * is only valid for the lifetime of the method call in which
 * instances of this class are passed.
 */
public abstract class RegisterMapper {

    /**
     * Gets the count of registers (really, the total register width, since
     * category width is counted) in the new namespace.
     * @return >= 0 width of new namespace.
     */
    public abstract int getNewRegisterCount();

    /**
     * @param registerSpec old register
     * @return register in new space
     */
    public abstract RegisterSpec map(RegisterSpec registerSpec);

    /**
     *
     * @param sources old register list
     * @return new mapped register list, or old if nothing has changed.
     */
    public final RegisterSpecList map(RegisterSpecList sources) {
        int sz = sources.size();
        RegisterSpecList newSources = new RegisterSpecList(sz);
        for (int i = 0; i < sz; i++) {
            newSources.set(i, map(sources.get(i)));
        }
        newSources.setImmutable();
        return newSources.equals(sources) ? sources : newSources;
    }
}
