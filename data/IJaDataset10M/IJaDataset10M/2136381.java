package pl.bristleback.server.bristle.action;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.action.Response;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 11:48:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public class DefaultActionInformation<T> extends AbstractActionInformation<DefaultAction<T>> {

    private static Logger log = Logger.getLogger(DefaultActionInformation.class.getName());

    private static final int CONNECTOR_ACTION_PARAMETER = 0;

    private static final int SECOND_ACTION_PARAMETER = 1;

    public DefaultActionInformation(String name) {
        super(name);
    }

    public boolean isDefaultAction() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public Response execute(DefaultAction<T> actionClass, Object[] parameters) throws Exception {
        return actionClass.executeDefault((WebsocketConnector) parameters[CONNECTOR_ACTION_PARAMETER], (T) parameters[SECOND_ACTION_PARAMETER]);
    }
}
