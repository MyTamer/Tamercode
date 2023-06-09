package seqSamoa.protocols.fd;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import seqSamoa.Message;
import seqSamoa.ProtocolModule;
import seqSamoa.ProtocolStack;
import seqSamoa.Service;
import seqSamoa.ServiceCallOrResponse;
import seqSamoa.AtomicTask;
import seqSamoa.exceptions.AlreadyExistingProtocolModuleException;
import seqSamoa.exceptions.NotScheduledTaskException;
import seqSamoa.services.fd.FD;
import seqSamoa.services.fd.FDCallParameters;
import seqSamoa.services.fd.FDResponseParameters;
import seqSamoa.services.udp.UDP;
import seqSamoa.services.udp.UDPCallParameters;
import uka.transport.Transportable;
import framework.Constants;
import framework.GroupCommEventArgs;
import framework.GroupCommException;
import framework.PID;
import framework.libraries.Timer;
import framework.libraries.Trigger;
import framework.libraries.serialization.TSet;
import groupcomm.common.fd.FDHandler;

/**
 * This class implement a Protocol that detect distant process failure. It
 * implements the failure detector needed by the consensus protocol described by
 * Chandra and Toueg.
 * 
 * This Protocol need a Protocol that implements UDP transport.
 * 
 * The service implemented is FD (described in util/Services.java)
 */
public class ProtocolPing extends ProtocolModule implements Trigger, Timer {

    static final int MAX_PROCESSES = 7;

    private FD fd;

    private Service<UDPCallParameters, Object> udp;

    private FDHandler handlers = null;

    private Map<Transportable, AtomicTask> timers = null;

    protected FD.Executer fdExecuter;

    protected UDP.Listener udpListener;

    private ServiceCallOrResponse fdCallCOR;

    /**
     * Constructor. <br>
     * 
     * @param name
     *            Name of the layer
     * @param stack
     * 			  The stack in which the module will be
     * @param timeout
     *            The timeout for ping messages
     * @param retries
     *            The number of retries before suspecting
     */
    @SuppressWarnings("unchecked")
    public ProtocolPing(String name, ProtocolStack stack, int timeout, int retries, FD fd, Service<? extends UDPCallParameters, ? extends Object> udp) throws AlreadyExistingProtocolModuleException {
        super(name, stack);
        handlers = new FDHandler(this, this, stack.getPID(), timeout, retries);
        timers = new HashMap<Transportable, AtomicTask>();
        this.fd = fd;
        this.udp = (Service<UDPCallParameters, Object>) udp;
        this.fdCallCOR = ServiceCallOrResponse.createServiceCallOrResponse(this.fd, true);
        LinkedList<ServiceCallOrResponse> initiatedFD = new LinkedList<ServiceCallOrResponse>();
        for (int i = 0; i < MAX_PROCESSES; i++) initiatedFD.add(ServiceCallOrResponse.createServiceCallOrResponse(udp, true));
        initiatedFD.add(ServiceCallOrResponse.createServiceCallOrResponse(fd, false));
        initiatedFD.add(ServiceCallOrResponse.createServiceCallOrResponse(fd, false));
        fdExecuter = fd.new Executer(this, initiatedFD) {

            public void evaluate(FDCallParameters params, Message dmessage) {
                synchronized (this.parent) {
                    GroupCommEventArgs ga = new GroupCommEventArgs();
                    ga.addLast(params.startMonitoring);
                    ga.addLast(params.stopMonitoring);
                    try {
                        handlers.handleStartStopMonitor(ga);
                    } catch (GroupCommException ex) {
                        throw new RuntimeException("ProtocolPing: fdExecuter: " + ex.getMessage());
                    }
                }
            }
        };
        LinkedList<ServiceCallOrResponse> initiatedUdp = new LinkedList<ServiceCallOrResponse>();
        initiatedUdp.add(ServiceCallOrResponse.createServiceCallOrResponse(udp, true));
        initiatedUdp.add(ServiceCallOrResponse.createServiceCallOrResponse(fd, false));
        udpListener = this.udp.new Listener(this, initiatedUdp) {

            public void evaluate(Object infos, Transportable response) {
                synchronized (this.parent) {
                    GroupCommEventArgs ga = new GroupCommEventArgs();
                    ga.addLast(response);
                    handlers.handleAlive(ga);
                }
            }
        };
    }

    public synchronized void dump(OutputStream stream) {
        handlers.dump(stream);
    }

    /**
     * Manage the triggering of the events
     */
    public void trigger(int type, GroupCommEventArgs e) {
        FDResponseParameters infos;
        switch(type) {
            case Constants.ALIVE:
                Transportable message = e.remove(0);
                UDPCallParameters params = new UDPCallParameters((PID) e.remove(0));
                udp.call(params, new Message(message, udpListener));
                break;
            case Constants.SUSPECT:
                infos = new FDResponseParameters((TSet) e.remove(0));
                fd.response(infos, null);
                break;
            default:
                throw new RuntimeException("ProtocolPing: trigger: " + "Unexpected event type");
        }
    }

    public synchronized void schedule(final Transportable key, boolean periodic, int time) {
        if (!periodic) throw new RuntimeException("ProtocolPing: schedule: Non-periodic " + "timers not supported. Discarding it.");
        if (!timers.containsKey(key)) {
            AtomicTask trigger = new AtomicTask() {

                public void execute() {
                    timeout(key);
                }

                public ServiceCallOrResponse getCOR() {
                    return fdCallCOR;
                }
            };
            timers.put(key, trigger);
            stack.getScheduler().schedule(trigger, periodic, time);
        } else {
            throw new RuntimeException("ProtocolPing:schedule: Suspect " + "Task already scheduled!");
        }
    }

    public synchronized void cancel(Transportable key) {
        try {
            stack.getScheduler().cancel(timers.remove(key));
        } catch (NotScheduledTaskException ex) {
            throw new RuntimeException("ProtocolPing: cancel: The task is not" + " currently scheduled");
        }
    }

    public synchronized void reset(Transportable key) {
        try {
            stack.getScheduler().reset(timers.get(key));
        } catch (NotScheduledTaskException ex) {
            throw new RuntimeException("ProtocolPing: reset: The task is not" + " currently scheduled");
        }
    }

    private synchronized void timeout(Object o) {
        GroupCommEventArgs ga = new GroupCommEventArgs();
        final Transportable key = (Transportable) o;
        if (!timers.containsKey(key)) return;
        ga.add(key);
        handlers.handleTimeOut(ga);
    }
}
