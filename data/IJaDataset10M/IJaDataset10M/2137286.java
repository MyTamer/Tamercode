package plugin.qualifier.pobject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.PrimitiveChoiceFilter;
import pcgen.character.CharacterDataStore;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.ChooseLstQualifierToken;
import pcgen.util.Logging;

public class PCToken<T extends CDOMObject> implements ChooseLstQualifierToken<T> {

    private Class<T> refClass;

    private PrimitiveChoiceFilter<T> pcs = null;

    public String getTokenName() {
        return "PC";
    }

    public boolean initialize(LoadContext context, Class<T> cl, String condition, String value) {
        if (condition != null) {
            Logging.addParseMessage(Level.SEVERE, "Cannot make " + getTokenName() + " into a conditional Qualifier, remove =");
            return false;
        }
        if (cl == null) {
            throw new IllegalArgumentException();
        }
        refClass = cl;
        if (value != null) {
            pcs = context.getPrimitiveChoiceFilter(cl, value);
            return pcs != null;
        }
        return true;
    }

    public Class<? super T> getChoiceClass() {
        if (refClass == null) {
            return CDOMObject.class;
        } else {
            return refClass;
        }
    }

    public Set<T> getSet(CharacterDataStore pc) {
        List<T> objects = pc.getActiveGraph().getGrantedNodeList(refClass);
        Set<T> returnSet = new HashSet<T>();
        if (objects != null && pcs != null) {
            for (T po : objects) {
                if (pcs.allow(pc, po)) {
                    returnSet.add(po);
                }
            }
        }
        return returnSet;
    }

    public String getLSTformat() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTokenName());
        if (pcs != null) {
            sb.append('[').append(pcs.getLSTformat()).append(']');
        }
        return sb.toString();
    }
}
