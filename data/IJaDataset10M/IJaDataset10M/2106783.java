package br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine;

import java.util.List;
import java.util.Map;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.DriverData;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDevice;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.Notify;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceCall;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;

public interface Gateway {

    public ServiceResponse callService(UpDevice device, String serviceName, String driverName, String instanceId, String securityType, Map<String, String> parameters) throws ServiceCallException;

    public ServiceResponse callService(UpDevice device, ServiceCall serviceCall) throws ServiceCallException;

    /**
	 * Register a Listener for a event, driver and device specified.
	 * 
	 * @param listener UosEventListener responsible for dealing with the event.
	 * @param device Device which event must be listened
	 * @param driver Driver responsible for the event.
	 * @param eventKey EventKey that identifies the wanted event to be listened.
	 * @throws NotifyException In case of an error.
	 */
    public void registerForEvent(UosEventListener listener, UpDevice device, String driver, String eventKey) throws NotifyException;

    /**
	 * Register a Listener for a event, driver and device specified.
	 * 
	 * @param listener UosEventListener responsible for dealing with the event.
	 * @param device Device which event must be listened
	 * @param driver Driver responsible for the event.
	 * @param instanceId Instance Identifier of the driver to be registered upon.
	 * @param eventKey EventKey that identifies the wanted event to be listened.
	 * @throws NotifyException In case of an error.
	 */
    public void registerForEvent(UosEventListener listener, UpDevice device, String driver, String instanceId, String eventKey) throws NotifyException;

    /**
	 * Removes a listener for receiving Notify events and notifies the event driver of its removal.
	 * 
	 * @param listener Listener to be removed.
	 * @throws NotifyException
	 */
    public void unregisterForEvent(UosEventListener listener) throws NotifyException;

    /**
	 * Removes a listener for receiving Notify events and notifies the event driver of its removal.
	 * 
	 * @param listener Listener to be removed.
	 * @param driver Driver from which the listener must be removed (If not informed all drivers will be considered).
	 * @param instanceId InstanceId from the Driver which the listener must be removed (If not informed all instances will be considered).
	 * @param eventKey EventKey from which the listener must be removed (If not informed all events will be considered).
	 * @throws NotifyException
	 */
    public void unregisterForEvent(UosEventListener listener, UpDevice device, String driver, String instanceId, String eventKey) throws NotifyException;

    /**
	 * Sends a notify message to the device informed.
	 * 
	 * @param notify Notify message to be sent.
	 * @param device Device which is going to receive the notofy event
	 * @throws MessageEngineException
	 */
    public void sendEventNotify(Notify notify, UpDevice device) throws NotifyException;

    /**
	 * @return Data about the Current Device uOS is running on.
	 */
    public UpDevice getCurrentDevice();

    /**
	 * Returns the list of drivers know in the database.
	 * 
	 * @param driverName Optional filter for the query.
	 * @return list of drivers known in the database.
	 */
    public List<DriverData> listDrivers(String driverName);
}
