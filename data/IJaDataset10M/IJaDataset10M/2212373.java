package fitgoodies;

import java.lang.reflect.Method;
import fit.Fixture;
import fit.Parse;
import fit.TypeAdapter;
import fitgoodies.util.FixtureTools;
import fitgoodies.util.WaitForResult;

/**
 * In contrast to <code>fit.ActionFixture</code>, this <code>ActionFixture</code>
 * enables all fitgoodies features (for example custom type adapters,
 * custom parsers and cross references). <br /><br />
 *
 * In addition, <code>waitFor</code> was added. The command takes a method name
 * without parameters and a return value of <code>Boolean</code> as the first
 * parameter and a timeout in ms as the second parameter.<br />
 * The method is called every <code>sleepTime</code> ms, until it returns true or the timeout is
 * exceeded.
 *
 * @author jwierum
 * @version $Id: ActionFixture.java 203 2009-08-24 12:03:16Z jwierum $
 */
public class ActionFixture extends fit.ActionFixture {

    private static final int DEFAULT_SLEEP_TIME = 100;

    private String parameter;

    /**
	 * Sets the actor of a fixture.
	 *
	 * At runtime, the actor can be set using <code>start</code> in the input
	 * table.
	 * @param fixture the new actor
	 */
    protected static void setActor(final Fixture fixture) {
        actor = fixture;
    }

    /**
	 * Creates a new ActionFixture. The actor of this fixture defaults to "this".
	 */
    public ActionFixture() {
        setActor(this);
    }

    /**
	 * Replacement of <code>check</code> which resolves cross-references
	 * before calling the original check method of fit.
	 *
	 *  @param cell the cell to check
     *  @param a - TypeAdapter to use
     *
     *  @see fit.Fixture#check {@link fit.Fixture#check(Parse, TypeAdapter)}
     */
    @Override
    public void check(final Parse cell, final TypeAdapter a) {
        TypeAdapter ta = FixtureTools.rebindTypeAdapter(a, parameter);
        ta = FixtureTools.processCell(cell, ta, this);
        if (ta != null) {
            super.check(cell, ta);
        }
    }

    /**
	 * Replacement of <code>enter()</code> which resolves cross-references
	 * and calls the original <code>fit.enter()</code>.
	 *
	 * @see fit.ActionFixture#enter() {@link fit.ActionFixture#enter()}
	 */
    @Override
    public void enter() throws Exception {
        Method method = method(1);
        Class<?> type = method.getParameterTypes()[0];
        TypeAdapter ta = getTypeAdapter(type);
        if (ta != null) {
            Object[] args = { ta.parse(cells.more.more.text()) };
            method.invoke(actor, args);
        }
    }

    private TypeAdapter getTypeAdapter(final Class<?> type) {
        TypeAdapter ta = TypeAdapter.on(actor, type);
        ta = FixtureTools.rebindTypeAdapter(ta, parameter);
        ta = FixtureTools.processCell(cells.more.more, ta, this);
        return ta;
    }

    private long getSleepTime(final TypeAdapter typeAdapter) throws Exception {
        long sleepTime = DEFAULT_SLEEP_TIME;
        if (cells.more.more.more != null) {
            sleepTime = (Long) typeAdapter.parse(cells.more.more.more.text());
        }
        return sleepTime;
    }

    private void writeResultIntoCell(final WaitForResult waitForResult) {
        cells.more.more.body = Long.toString(waitForResult.getLastElapsedTime());
        if (waitForResult.lastCallWasSuccessfull()) {
            right(cells.more.more);
        } else {
            wrong(cells.more.more);
            info(cells.more.more, "(Timeout)");
        }
    }

    /**
	 * Waits until a method returns true. The command takes a method name
	 * without parameters and a return value of <code>Boolean</code> as the first
	 * parameter and a timeout in ms as the second parameter.<br />
	 * The method is called every <code>sleepTime</code>ms, until it returns true or the timeout is
	 * exceeded.
	 * @throws Exception propagated to fit
	 */
    public void waitFor() throws Exception {
        Method method = method(0);
        TypeAdapter typeAdapter = getTypeAdapter(Long.class);
        long maxTime = (Long) typeAdapter.parse(cells.more.more.text());
        long sleepTime = getSleepTime(typeAdapter);
        WaitForResult waitForResult = new WaitForResult(method, actor, maxTime);
        waitForResult.setSleepTime(sleepTime);
        waitForResult.repeatInvokeWithTimeout();
        writeResultIntoCell(waitForResult);
    }

    /**
	 * Transforms the selected row into an &quot;enter&quot; command and
	 * reinterprets it.<br /><br />
	 *
	 * Example: <br />
	 * Row content: <code>setEndocing | utf-8</code><br />
	 * Code in the fixture:
	 * <code><pre>
	 * public void setEncoding() throws Exception {
	 *     transformAndEnter();
	 * }
	 *
	 * public void setEncoding(String encoding) {
	 *     // do stuff with encoding here
	 * }
	 * </pre></code>
	 *
	 * @throws Exception should be propagated to fit.
	 */
    protected final void transformAndEnter() throws Exception {
        Parse oldmore = cells.more;
        cells.more = new Parse("<td></td>", new String[] { "td" });
        cells.more.body = cells.body;
        cells.more.more = oldmore;
        cells.body = "enter";
        Fixture oldActor = fit.ActionFixture.actor;
        setActor(this);
        enter();
        setActor(oldActor);
        cells.body = cells.more.body;
        cells.more = cells.more.more;
    }

    /**
	 * Replacement of <code>parse</code> which uses the extended parse features of
	 * fitgoodies and uses fit's parse as a fallback.
	 *
	 *  @param text text to parse
     *  @param type type to transform text to
     *
     *  @return Object of type <code>type</code> which represents <code>text</code>
     *  @throws Exception if the value can't be parsed
     *
     *  @see fit.Fixture#parse(String, Class) {@link fit.Fixture#parse(String, Class)}
	 */
    @Override
    @SuppressWarnings("unchecked")
    public Object parse(final String text, final Class type) throws Exception {
        Object result = FixtureTools.parse(text, type, parameter);
        if (result == null) {
            return super.parse(text, type);
        } else {
            return result;
        }
    }

    /**
	 * Sets the fixture parameters.
	 *
	 * Normally, these values are generated by reading the first
	 * line of the table. This method is primary useful for debugging.
	 * You won't need it otherwise.
	 *
	 * @param parameters parameters to store in <code>args</code>
	 */
    public final void setParams(final String[] parameters) {
        this.args = parameters;
    }

    /**
	 * Initializes the fixture arguments, call <code>setUp</code>,
	 * <code>fit.ActionFixture.doTable(Parse)</code> and <code>tearDown()</code>.
	 *
     * @param table the table to be processed
	 * @see fit.Fixture#doTable(Parse) {@link fit.Fixture#doTable(Parse)}
	 */
    @Override
    public void doTable(final Parse table) {
        FixtureTools.copyParamsToFixture(args, this);
        try {
            setUp();
            try {
                super.doTable(table);
            } catch (Exception e) {
                exception(table.parts.parts, e);
            }
            tearDown();
        } catch (Exception e) {
            exception(table.parts.parts, e);
        }
    }

    /**
     * Does nothing. Override it to initialize the fixture.
     * The method is called before doTables.
     * @throws Exception any kind of exception aborts the execution of this fixture
     */
    public void setUp() throws Exception {
    }

    /**
     * Does nothing. Override it to tear down the fixture.
     * The method is called after doTables.
     *
     * @throws Exception any kind of exception aborts the execution of this fixture
     */
    public void tearDown() throws Exception {
    }

    /**
     * Looks up a given parameter in the fixture's argument list.
     *
     * @param paramName the parameter name to look up
     * @return  the parameter value, if it could be found, <code>null</code> otherwise
     * @see #getParam(String, String) {@link #getParam(String, String)}
     * @see FixtureTools#getArg(String[], String, String)
     * 		{@link FixtureTools#getArg(String[], String, String)}
     */
    public final String getParam(final String paramName) {
        return getParam(paramName, null);
    }

    /**
	 * Looks up a given parameter in the fixture's argument list.
	 *
	 * If the value does not exist, the given default value is returned.
     * @param paramName paramName the parameter name to look up
     * @param defaultValue defaultValue the value to be returned if the parameter is missing
     * @return the parameter value, if it could be found, <code>defaultValue</code> otherwise
	 */
    public final String getParam(final String paramName, final String defaultValue) {
        return FixtureTools.getArg(args, paramName, defaultValue);
    }

    @Override
    public void doRow(final Parse row) {
        parameter = FixtureTools.extractCellParameter(row.parts);
        super.doRow(row);
    }
}
