package pcgen.core.npcgen;

import java.util.List;
import pcgen.core.Globals;
import pcgen.core.Skill;
import pcgen.util.WeightedList;

/**
 * This class represents a potential skill choice.  The class is needed because
 * PCGen does not treat skill groups specially in the code but the user expects
 * to treat them special.
 * 
 * <p>As an example, if the user specifies that TYPE.Profession skills are to 
 * have a certain weight the assumption is that that weight applies to picking
 * a single Profession skill and not to each Profession skill individually.
 * 
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.11.1
 */
public class SkillChoice {

    private String theKey = null;

    private List<Skill> theSkillList = new WeightedList<Skill>();

    /**
	 * Creates a new SkillChoice.
	 * 
	 * <p>If the key passed in starts with &quot;<code>TYPE.</code>&quot;, the
	 * group of skills of that type will be stored as this chice.
	 * 
	 * @param aKey A Skill key or TYPE.&lt;skill type&gt;
	 */
    public SkillChoice(final String aKey) {
        theKey = aKey;
        if (theKey.startsWith("TYPE")) {
            final List<Skill> subSkills = Globals.getSkillsByType(theKey.substring(5));
            theSkillList.addAll(subSkills);
        } else {
            theSkillList.add(Globals.getSkillKeyed(theKey));
        }
    }

    /**
	 * Gets the skill associated with this chioce.  If this choice is a group
	 * of choices, the specific skill will be selected randomly.
	 * 
	 * @return A <tt>Skill</tt>
	 */
    public Skill getSkill() {
        final Skill skill = theSkillList.get(Globals.getRandomInt(theSkillList.size()));
        theSkillList.add(NPCGenerator.getSubSkillWeightAdd(), skill);
        return skill;
    }

    /**
	 * Checks if this <tt>SkillChoice</tt> has the specified skill as an option.
	 * That is, if this skill represents the same skill or if the skill is in
	 * the list of possible skill choices.
	 * 
	 * @param aKey The Skill key to check.
	 * 
	 * @return <tt>true</tt> if this choice contains the skill.
	 */
    public boolean hasSkill(final String aKey) {
        if (theKey.equals(aKey)) {
            return true;
        }
        if (theSkillList.size() == 1) {
            return false;
        }
        final Skill skill = Globals.getSkillKeyed(aKey);
        if (skill == null) {
            return false;
        }
        for (final Skill s : theSkillList) {
            if (s.getKeyName().equals(aKey)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return theSkillList.toString();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((theKey == null) ? 0 : theKey.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SkillChoice other = (SkillChoice) obj;
        if (theKey == null) {
            if (other.theKey != null) return false;
        } else if (!theKey.equals(other.theKey)) return false;
        return true;
    }
}
