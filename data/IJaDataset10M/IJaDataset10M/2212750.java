package com.planet_ink.coffee_mud.Commands;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class Description extends StdCommand {

    public Description() {
    }

    private String[] access = { "DESCRIPTION" };

    public String[] getAccessWords() {
        return access;
    }

    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        if (commands.size() < 2) {
            mob.tell("^xYour current description:^?\n\r" + mob.description());
            mob.tell("\n\rEnter DESCRIPTION [NEW TEXT] to change.");
            return false;
        }
        String s = CMParms.combine(commands, 1);
        if (s.length() > 255) mob.tell("Your description exceeds 255 characters in length.  Please re-enter a shorter one."); else {
            mob.setDescription(s);
            mob.tell("Your description has been changed.");
        }
        return false;
    }

    public boolean canBeOrdered() {
        return false;
    }
}
