package de.johannesluderschmidt.throng.proxy;

import java.util.Vector;
import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import de.johannesluderschmidt.throng.util.Constants;

public class ThrongCropper {

    private static ThrongCropper inst;

    private Vector<Host> hosts;

    private Vector<Host> touchHosts;

    private Vector<Host> fiducialHosts;

    private boolean enabled;

    private ThrongCropper() {
    }

    public static ThrongCropper getInstance() {
        if (inst == null) {
            inst = new ThrongCropper();
        }
        return inst;
    }

    public OSCBundle globalizeTuioBundle(OSCBundle bundle, int inputPort) {
        if (enabled) {
            String address = bundle.getIpAddress().toString().substring(1, bundle.getIpAddress().toString().length());
            int posInBundle = 0;
            for (OSCPacket packet : bundle.getPackets()) {
                OSCMessage message = (OSCMessage) packet;
                if (message.getArguments()[0].equals("set") && message.getAddress().equals(Constants.TWOD_CUR)) {
                    Object[] newArguments = globalizeTouch(message.getArguments(), address, inputPort);
                    packet = new OSCMessage(message.getAddress(), newArguments);
                    bundle = createNewBundle(bundle, packet, posInBundle);
                }
                if (message.getArguments()[0].equals("set") && message.getAddress().equals(Constants.TWOD_OBJ)) {
                    Object[] newArguments = globalizeFidu(message.getArguments(), address, inputPort);
                    packet = new OSCMessage(message.getAddress(), newArguments);
                    bundle = createNewBundle(bundle, packet, posInBundle);
                }
                posInBundle = posInBundle + 1;
            }
        }
        return bundle;
    }

    private OSCBundle createNewBundle(OSCBundle oldBundle, OSCPacket newPacket, int packetPosition) {
        OSCPacket[] newPackets = new OSCPacket[oldBundle.getPackets().length];
        for (int i = 0; i < oldBundle.getPackets().length; i++) {
            if (i != packetPosition) {
                newPackets[i] = oldBundle.getPackets()[i];
            } else {
                newPackets[i] = newPacket;
            }
        }
        OSCBundle newBundle = new OSCBundle(newPackets);
        newBundle.setIpAddress(oldBundle.getIpAddress());
        newBundle.setPortOut(oldBundle.getPortOut());
        return newBundle;
    }

    private Object[] globalizeTouch(Object[] payloadArray, String address, int inputPort) {
        int i = 0;
        float width = 0;
        int xPosIndex = 2;
        int yPosIndex = 3;
        if (Config.getInstance().getUseOffsets()) {
            width = Config.getInstance().getOffsetXLeft();
        }
        for (Host host : touchHosts) {
            if (address.equals(host.getIp()) && host.getPort() == inputPort) {
                if (host.getFlipXAndY()) {
                    Float xValueTemp = (Float) payloadArray[xPosIndex];
                    payloadArray[xPosIndex] = (Float) payloadArray[yPosIndex];
                    payloadArray[yPosIndex] = xValueTemp;
                }
                if (Config.getInstance().getUseOffsets()) {
                    float finalWidth = width + ((Float) payloadArray[xPosIndex]) * host.getWidth();
                    payloadArray[xPosIndex] = finalWidth;
                }
                break;
            } else {
                if (Config.getInstance().getUseOffsets()) {
                    width = width + host.getWidth();
                }
            }
            i = i + 1;
        }
        return payloadArray;
    }

    private Object[] globalizeFidu(Object[] payloadArray, String address, int inputPort) {
        int i = 1;
        float width = 0;
        if (!Config.getInstance().isFlipXAndY()) {
            width = Config.getInstance().getOffsetXLeft();
        } else {
            width = Config.getInstance().getOffsetYTop();
        }
        for (Host host : fiducialHosts) {
            if (address.equals(host.getIp()) && host.getPort() == inputPort) {
                int xPosIndex = 3;
                int yPosIndex = 4;
                if (host.getFlipXAndY()) {
                    Float xValueTemp = (Float) payloadArray[xPosIndex];
                    payloadArray[xPosIndex] = (Float) payloadArray[yPosIndex];
                    payloadArray[yPosIndex] = xValueTemp;
                }
                if (host.getFlipRotation()) {
                    payloadArray[5] = -((Float) payloadArray[5]);
                }
                if (Config.getInstance().getUseOffsets()) {
                    if (((Float) payloadArray[yPosIndex]) > host.getCropStartY() && ((Float) payloadArray[yPosIndex]) < host.getCropEndY()) {
                        float finalWidth = width + ((Float) payloadArray[xPosIndex]) * host.getWidth();
                        payloadArray[xPosIndex] = finalWidth;
                    } else {
                        payloadArray = null;
                    }
                }
                break;
            } else {
                width = width + host.getWidth();
            }
            i = i + 1;
        }
        return payloadArray;
    }

    public Vector<Host> getHosts() {
        return hosts;
    }

    public void setHosts(Vector<Host> hosts) {
        touchHosts = new Vector<Host>();
        fiducialHosts = new Vector<Host>();
        for (Host host : hosts) {
            if (host.getType().equals(Constants.TOUCH)) {
                touchHosts.add(host);
            }
            if (host.getType().equals(Constants.FIDUCIAL)) {
                fiducialHosts.add(host);
            }
            if (host.getType().equals(Constants.BOTH)) {
                fiducialHosts.add(host);
                touchHosts.add(host);
            }
        }
        this.hosts = hosts;
        setEnabled(true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
