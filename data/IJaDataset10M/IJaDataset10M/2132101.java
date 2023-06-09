package net.sf.mmm.util.pojo.descriptor.base.accessor;

import net.sf.mmm.util.pojo.descriptor.api.accessor.PojoPropertyAccessorOneArg;
import net.sf.mmm.util.pojo.descriptor.api.accessor.PojoPropertyAccessorOneArgMode;

/**
 * This is the interface used to create a {@link PojoPropertyAccessorOneArg
 * one-arg accessor}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.1.0
 */
public interface PojoPropertyAccessorOneArgBuilder extends PojoPropertyAccessorBuilder<PojoPropertyAccessorOneArg> {

    /**
   * {@inheritDoc}
   */
    PojoPropertyAccessorOneArgMode getMode();
}
