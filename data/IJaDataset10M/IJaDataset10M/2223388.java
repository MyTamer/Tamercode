package com.meidusa.amoeba.sqljep.function;

import java.util.Calendar;
import java.sql.*;
import static java.util.Calendar.*;
import com.meidusa.amoeba.sqljep.function.PostfixCommand;
import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;

public class SubTime extends PostfixCommand {

    public final int getNumberOfParameters() {
        return 2;
    }

    /**
	 * Calculates the result of applying the "+" operator to the arguments from
	 * the stack and pushes it back on the stack.
	 */
    public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
        node.childrenAccept(runtime.ev, null);
        Comparable<?> param2 = runtime.stack.pop();
        Comparable<?> param1 = runtime.stack.pop();
        return new Comparable<?>[] { param1, param2 };
    }

    public static java.util.Date subTime(Comparable<?> param1, Comparable<?> param2, Calendar cal) throws ParseException {
        if (param1 == null || param2 == null) {
            return null;
        }
        if ((param1 instanceof Time || param1 instanceof Timestamp) && param2 instanceof Time) {
            java.util.Date d1 = (java.util.Date) param1;
            java.util.Date d2 = (java.util.Date) param2;
            cal.setTimeInMillis(d2.getTime());
            int h = cal.get(HOUR_OF_DAY);
            int m = cal.get(MINUTE);
            int s = cal.get(SECOND);
            cal.setTimeInMillis(d1.getTime());
            cal.add(SECOND, -s);
            cal.add(MINUTE, -m);
            cal.roll(HOUR_OF_DAY, -h);
            return (param1 instanceof Time) ? new Time(cal.getTimeInMillis()) : new Timestamp(cal.getTimeInMillis());
        } else {
            throw new ParseException(WRONG_TYPE + "  subtime(" + param1.getClass() + "," + param2.getClass() + ")");
        }
    }

    public Comparable<?> getResult(Comparable<?>... comparables) throws ParseException {
        return subTime(comparables[0], comparables[1], JepRuntime.getCalendar());
    }
}
