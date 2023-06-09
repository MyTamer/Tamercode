package com.hifi.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/** Another utility class the encapsulates some complexity.
 *  Keep in mind that I very well could have used Endpoint here, but I
 *  decided against it mainly so I could do validity checking.
 *  This may be a bad decision.  I'm sure someone will let me know during
 *  code review.
 */
public class IPPortCombo implements IpPort {

    private int _port;

    private InetAddress _addr;

    public static final String DELIM = ":";

    /**
     * Used for reading data from the network.  Throws BadPacketException
     * if the data is invalid.
     * @param fromNetwork 6 bytes - first 4 are IP, next 2 are port
     */
    public static IPPortCombo getCombo(byte[] fromNetwork) throws BadPacketException {
        return new IPPortCombo(fromNetwork);
    }

    /**
     * Constructor used for data read from the network.
     * Throws BadPacketException on errors.
     */
    private IPPortCombo(byte[] networkData) throws BadPacketException {
        if (networkData.length != 6) throw new BadPacketException("Weird Input");
        int port = ByteOrder.ushort2int(ByteOrder.leb2short(networkData, 4));
        if (!NetworkUtils.isValidPort(port)) throw new BadPacketException("Bad Port: " + port);
        _port = port;
        try {
            byte[] a = new byte[4];
            a[0] = networkData[0];
            a[1] = networkData[1];
            a[2] = networkData[2];
            a[3] = networkData[3];
            _addr = InetAddress.getByAddress(a);
        } catch (UnknownHostException uhe) {
            throw new BadPacketException("bad host.");
        }
        if (!NetworkUtils.isValidAddress(_addr)) throw new BadPacketException("invalid addr: " + _addr);
    }

    /**
     * Constructor used for local data.
     * Throws IllegalArgumentException on errors.
     */
    public IPPortCombo(String hostAddress, int port) throws UnknownHostException, IllegalArgumentException {
        if (!NetworkUtils.isValidPort(port)) throw new IllegalArgumentException("Bad Port: " + port);
        _port = port;
        _addr = InetAddress.getByName(hostAddress);
        if (!NetworkUtils.isValidAddress(_addr)) throw new IllegalArgumentException("invalid addr: " + _addr);
    }

    public int getPort() {
        return _port;
    }

    public InetAddress getInetAddress() {
        return _addr;
    }

    public String getAddress() {
        return _addr.getHostAddress();
    }

    /** @return the ip and port encoded in 6 bytes (4 ip, 2 port).
     *  //TODO if IPv6 kicks in, this may fail, don't worry so much now.
     */
    public byte[] toBytes() {
        byte[] retVal = new byte[6];
        for (int i = 0; i < 4; i++) retVal[i] = _addr.getAddress()[i];
        ByteOrder.short2leb((short) _port, retVal, 4);
        return retVal;
    }

    public boolean equals(Object other) {
        if (other instanceof IPPortCombo) {
            IPPortCombo combo = (IPPortCombo) other;
            return _addr.equals(combo._addr) && (_port == combo._port);
        }
        return false;
    }

    public int hashCode() {
        return _addr.hashCode() * _port;
    }

    public String toString() {
        return getAddress() + ":" + getPort();
    }
}
