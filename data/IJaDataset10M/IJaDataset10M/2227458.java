package net.sf.battlefieldjava.samples.first;

import au.com.noojee.battlefieldjava.defaultPieces.DefaultKnight;
import au.com.noojee.battlefieldjava.ruler.IKnight;
import au.com.noojee.battlefieldjava.ruler.IWorld;

/**
 * @author bsutton
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyKnight extends DefaultKnight implements IKnight {

    public MyKnight(IWorld world) {
        super(world);
    }
}
