package com.aelitis.azureus.core.dht.transport.udp.impl;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.core3.util.SHA1Simple;
import org.gudy.azureus2.core3.util.SystemTime;
import com.aelitis.azureus.core.dht.DHT;
import com.aelitis.azureus.core.dht.impl.DHTLog;
import com.aelitis.azureus.core.dht.netcoords.DHTNetworkPosition;
import com.aelitis.azureus.core.dht.netcoords.DHTNetworkPositionManager;
import com.aelitis.azureus.core.dht.transport.DHTTransportContact;
import com.aelitis.azureus.core.dht.transport.DHTTransportException;
import com.aelitis.azureus.core.dht.transport.DHTTransportFullStats;
import com.aelitis.azureus.core.dht.transport.DHTTransportValue;
import com.aelitis.azureus.core.dht.transport.udp.DHTTransportUDP;

/**
 * @author parg
 *
 */
public class DHTUDPUtils {

    protected static final int CT_UDP = 1;

    private static Map<String, byte[]> node_id_history = new LinkedHashMap<String, byte[]>(128, 0.75f, true) {

        protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
            return size() > 128;
        }
    };

    private static SHA1Simple hasher = new SHA1Simple();

    protected static byte[] getNodeID(InetSocketAddress address, byte protocol_version) throws DHTTransportException {
        InetAddress ia = address.getAddress();
        if (ia == null) {
            throw (new DHTTransportException("Address '" + address + "' is unresolved"));
        } else {
            String key;
            if (protocol_version >= DHTTransportUDP.PROTOCOL_VERSION_RESTRICT_ID3) {
                byte[] bytes = ia.getAddress();
                if (bytes.length == 4) {
                    final long K2 = 2500;
                    final long K3 = 50;
                    final long K4 = 5;
                    long result = address.getPort() % K4;
                    result = ((((long) bytes[3] << 8) & 0x000000ff00L) | result) % K3;
                    result = ((((long) bytes[2] << 16) & 0x0000ff0000L) | result) % K2;
                    result = ((((long) bytes[1] << 24) & 0x00ff000000L) | result);
                    result = ((((long) bytes[0] << 32) & 0xff00000000L) | result);
                    key = String.valueOf(result);
                } else {
                    key = ia.getHostAddress() + ":" + (address.getPort() % 8);
                }
            } else if (protocol_version >= DHTTransportUDP.PROTOCOL_VERSION_RESTRICT_ID_PORTS2) {
                key = ia.getHostAddress() + ":" + (address.getPort() % 8);
            } else if (protocol_version >= DHTTransportUDP.PROTOCOL_VERSION_RESTRICT_ID_PORTS) {
                key = ia.getHostAddress() + ":" + (address.getPort() % 1999);
            } else {
                key = ia.getHostAddress() + ":" + address.getPort();
            }
            synchronized (node_id_history) {
                byte[] res = node_id_history.get(key);
                if (res == null) {
                    res = hasher.calculateHash(key.getBytes());
                    node_id_history.put(key, res);
                }
                return (res);
            }
        }
    }

    protected static byte[] getBogusNodeID() {
        byte[] id = new byte[20];
        new Random().nextBytes(id);
        return (id);
    }

    protected static void serialiseLength(DataOutputStream os, int len, int max_length) throws IOException {
        if (len > max_length) {
            throw (new IOException("Invalid DHT data length: max=" + max_length + ",actual=" + len));
        }
        if (max_length < 256) {
            os.writeByte(len);
        } else if (max_length < 65536) {
            os.writeShort(len);
        } else {
            os.writeInt(len);
        }
    }

    protected static int deserialiseLength(DataInputStream is, int max_length) throws IOException {
        int len;
        if (max_length < 256) {
            len = is.readByte() & 0xff;
        } else if (max_length < 65536) {
            len = is.readShort() & 0xffff;
        } else {
            len = is.readInt();
        }
        if (len > max_length) {
            throw (new IOException("Invalid DHT data length: max=" + max_length + ",actual=" + len));
        }
        return (len);
    }

    protected static byte[] deserialiseByteArray(DataInputStream is, int max_length) throws IOException {
        int len = deserialiseLength(is, max_length);
        byte[] data = new byte[len];
        is.read(data);
        return (data);
    }

    protected static void serialiseByteArray(DataOutputStream os, byte[] data, int max_length) throws IOException {
        serialiseByteArray(os, data, 0, data.length, max_length);
    }

    protected static void serialiseByteArray(DataOutputStream os, byte[] data, int start, int length, int max_length) throws IOException {
        serialiseLength(os, length, max_length);
        os.write(data, start, length);
    }

    protected static void serialiseByteArrayArray(DataOutputStream os, byte[][] data, int max_length) throws IOException {
        serialiseLength(os, data.length, max_length);
        for (int i = 0; i < data.length; i++) {
            serialiseByteArray(os, data[i], max_length);
        }
    }

    protected static byte[][] deserialiseByteArrayArray(DataInputStream is, int max_length) throws IOException {
        int len = deserialiseLength(is, max_length);
        byte[][] data = new byte[len][];
        for (int i = 0; i < data.length; i++) {
            data[i] = deserialiseByteArray(is, max_length);
        }
        return (data);
    }

    public static final int INETSOCKETADDRESS_IPV4_SIZE = 7;

    public static final int INETSOCKETADDRESS_IPV6_SIZE = 19;

    protected static void serialiseAddress(DataOutputStream os, InetSocketAddress address) throws IOException, DHTTransportException {
        InetAddress ia = address.getAddress();
        if (ia == null) {
            Debug.out("Address '" + address + "' is unresolved");
            throw (new DHTTransportException("Address '" + address + "' is unresolved"));
        }
        serialiseByteArray(os, ia.getAddress(), 16);
        os.writeShort(address.getPort());
    }

    protected static InetSocketAddress deserialiseAddress(DataInputStream is) throws IOException {
        byte[] bytes = deserialiseByteArray(is, 16);
        int port = is.readShort() & 0xffff;
        return (new InetSocketAddress(InetAddress.getByAddress(bytes), port));
    }

    protected static DHTTransportValue[][] deserialiseTransportValuesArray(DHTUDPPacket packet, DataInputStream is, long skew, int max_length) throws IOException {
        int len = deserialiseLength(is, max_length);
        DHTTransportValue[][] data = new DHTTransportValue[len][];
        for (int i = 0; i < data.length; i++) {
            data[i] = deserialiseTransportValues(packet, is, skew);
        }
        return (data);
    }

    protected static void serialiseTransportValuesArray(DHTUDPPacket packet, DataOutputStream os, DHTTransportValue[][] values, long skew, int max_length) throws IOException, DHTTransportException {
        serialiseLength(os, values.length, max_length);
        for (int i = 0; i < values.length; i++) {
            serialiseTransportValues(packet, os, values[i], skew);
        }
    }

    public static final int DHTTRANSPORTCONTACT_SIZE = 2 + INETSOCKETADDRESS_IPV4_SIZE;

    protected static void serialiseContact(DataOutputStream os, DHTTransportContact contact) throws IOException, DHTTransportException {
        if (contact.getTransport() instanceof DHTTransportUDP) {
            os.writeByte(CT_UDP);
            os.writeByte(contact.getProtocolVersion());
            serialiseAddress(os, contact.getExternalAddress());
        } else {
            throw (new IOException("Unsupported contact type:" + contact.getClass().getName()));
        }
    }

    protected static DHTTransportUDPContactImpl deserialiseContact(DHTTransportUDPImpl transport, DataInputStream is) throws IOException, DHTTransportException {
        byte ct = is.readByte();
        if (ct != CT_UDP) {
            throw (new IOException("Unsupported contact type:" + ct));
        }
        byte version = is.readByte();
        InetSocketAddress external_address = deserialiseAddress(is);
        return (new DHTTransportUDPContactImpl(false, transport, external_address, external_address, version, 0, 0));
    }

    protected static DHTTransportValue[] deserialiseTransportValues(DHTUDPPacket packet, DataInputStream is, long skew) throws IOException {
        int len = deserialiseLength(is, 65535);
        List l = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            try {
                l.add(deserialiseTransportValue(packet, is, skew));
            } catch (DHTTransportException e) {
                Debug.printStackTrace(e);
            }
        }
        DHTTransportValue[] res = new DHTTransportValue[l.size()];
        l.toArray(res);
        return (res);
    }

    protected static void serialiseTransportValues(DHTUDPPacket packet, DataOutputStream os, DHTTransportValue[] values, long skew) throws IOException, DHTTransportException {
        serialiseLength(os, values.length, 65535);
        for (int i = 0; i < values.length; i++) {
            serialiseTransportValue(packet, os, values[i], skew);
        }
    }

    protected static DHTTransportValue deserialiseTransportValue(DHTUDPPacket packet, DataInputStream is, long skew) throws IOException, DHTTransportException {
        final int version;
        if (packet.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_REMOVE_DIST_ADD_VER) {
            version = is.readInt();
        } else {
            version = -1;
        }
        final long created = is.readLong() + skew;
        final byte[] value_bytes = deserialiseByteArray(is, DHT.MAX_VALUE_SIZE);
        final DHTTransportContact originator = deserialiseContact(packet.getTransport(), is);
        final int flags = is.readByte() & 0xff;
        final int life_hours;
        if (packet.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_LONGER_LIFE) {
            life_hours = is.readByte() & 0xff;
        } else {
            life_hours = 0;
        }
        final byte rep_control;
        if (packet.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_REPLICATION_CONTROL) {
            rep_control = is.readByte();
        } else {
            rep_control = DHT.REP_FACT_DEFAULT;
        }
        DHTTransportValue value = new DHTTransportValue() {

            public boolean isLocal() {
                return (false);
            }

            public long getCreationTime() {
                return (created);
            }

            public byte[] getValue() {
                return (value_bytes);
            }

            public int getVersion() {
                return (version);
            }

            public DHTTransportContact getOriginator() {
                return (originator);
            }

            public int getFlags() {
                return (flags);
            }

            public int getLifeTimeHours() {
                return (life_hours);
            }

            public byte getReplicationControl() {
                return (rep_control);
            }

            public byte getReplicationFactor() {
                return (rep_control == DHT.REP_FACT_DEFAULT ? DHT.REP_FACT_DEFAULT : (byte) (rep_control & 0x0f));
            }

            public byte getReplicationFrequencyHours() {
                return (rep_control == DHT.REP_FACT_DEFAULT ? DHT.REP_FACT_DEFAULT : (byte) (rep_control >> 4));
            }

            public String getString() {
                long now = SystemTime.getCurrentTime();
                return (DHTLog.getString(value_bytes) + " - " + new String(value_bytes) + "{v=" + version + ",f=" + Integer.toHexString(flags) + ",l=" + life_hours + ",r=" + Integer.toHexString(getReplicationControl()) + ",ca=" + (now - created) + ",or=" + originator.getString() + "}");
            }
        };
        return (value);
    }

    public static final int DHTTRANSPORTVALUE_SIZE_WITHOUT_VALUE = 17 + DHTTRANSPORTCONTACT_SIZE;

    protected static void serialiseTransportValue(DHTUDPPacket packet, DataOutputStream os, DHTTransportValue value, long skew) throws IOException, DHTTransportException {
        if (packet.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_REMOVE_DIST_ADD_VER) {
            int version = value.getVersion();
            os.writeInt(version);
        } else {
            os.writeInt(0);
        }
        os.writeLong(value.getCreationTime() + skew);
        serialiseByteArray(os, value.getValue(), DHT.MAX_VALUE_SIZE);
        serialiseContact(os, value.getOriginator());
        os.writeByte(value.getFlags());
        if (packet.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_LONGER_LIFE) {
            os.writeByte(value.getLifeTimeHours());
        }
        if (packet.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_REPLICATION_CONTROL) {
            os.writeByte(value.getReplicationControl());
        }
    }

    protected static void serialiseContacts(DataOutputStream os, DHTTransportContact[] contacts) throws IOException {
        serialiseLength(os, contacts.length, 65535);
        for (int i = 0; i < contacts.length; i++) {
            try {
                serialiseContact(os, contacts[i]);
            } catch (DHTTransportException e) {
                Debug.printStackTrace(e);
                throw (new IOException(e.getMessage()));
            }
        }
    }

    protected static DHTTransportContact[] deserialiseContacts(DHTTransportUDPImpl transport, DataInputStream is) throws IOException {
        int len = deserialiseLength(is, 65535);
        List l = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            try {
                l.add(deserialiseContact(transport, is));
            } catch (DHTTransportException e) {
                Debug.printStackTrace(e);
            }
        }
        DHTTransportContact[] res = new DHTTransportContact[l.size()];
        l.toArray(res);
        return (res);
    }

    protected static void serialiseVivaldi(DHTUDPPacketReply reply, DataOutputStream os) throws IOException {
        DHTNetworkPosition[] nps = reply.getNetworkPositions();
        if (reply.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_GENERIC_NETPOS) {
            os.writeByte((byte) nps.length);
            boolean v1_found = false;
            for (int i = 0; i < nps.length; i++) {
                DHTNetworkPosition np = nps[i];
                if (np.getPositionType() == DHTNetworkPosition.POSITION_TYPE_VIVALDI_V1) {
                    v1_found = true;
                }
                os.writeByte(np.getPositionType());
                os.writeByte(np.getSerialisedSize());
                np.serialise(os);
            }
            if (!v1_found) {
                Debug.out("Vivaldi V1 missing");
                throw (new IOException("Vivaldi V1 missing"));
            }
        } else {
            for (int i = 0; i < nps.length; i++) {
                if (nps[i].getPositionType() == DHTNetworkPosition.POSITION_TYPE_VIVALDI_V1) {
                    nps[i].serialise(os);
                    return;
                }
            }
            Debug.out("Vivaldi V1 missing");
            throw (new IOException("Vivaldi V1 missing"));
        }
    }

    protected static void deserialiseVivaldi(DHTUDPPacketReply reply, DataInputStream is) throws IOException {
        DHTNetworkPosition[] nps;
        if (reply.getProtocolVersion() >= DHTTransportUDP.PROTOCOL_VERSION_GENERIC_NETPOS) {
            int entries = is.readByte() & 0xff;
            nps = new DHTNetworkPosition[entries];
            int skipped = 0;
            for (int i = 0; i < entries; i++) {
                byte type = is.readByte();
                byte size = is.readByte();
                DHTNetworkPosition np = DHTNetworkPositionManager.deserialise(reply.getAddress().getAddress(), type, is);
                if (np == null) {
                    skipped++;
                    for (int j = 0; j < size; j++) {
                        is.readByte();
                    }
                } else {
                    nps[i] = np;
                }
            }
            if (skipped > 0) {
                DHTNetworkPosition[] x = new DHTNetworkPosition[entries - skipped];
                int pos = 0;
                for (int i = 0; i < nps.length; i++) {
                    if (nps[i] != null) {
                        x[pos++] = nps[i];
                    }
                }
                nps = x;
            }
        } else {
            nps = new DHTNetworkPosition[] { DHTNetworkPositionManager.deserialise(reply.getAddress().getAddress(), DHTNetworkPosition.POSITION_TYPE_VIVALDI_V1, is) };
        }
        boolean v1_found = false;
        for (int i = 0; i < nps.length; i++) {
            if (nps[i].getPositionType() == DHTNetworkPosition.POSITION_TYPE_VIVALDI_V1) {
                v1_found = true;
            }
        }
        if (!v1_found) {
            throw (new IOException("Vivaldi V1 missing"));
        }
        reply.setNetworkPositions(nps);
    }

    protected static void serialiseStats(int version, DataOutputStream os, DHTTransportFullStats stats) throws IOException {
        os.writeLong(stats.getDBValuesStored());
        os.writeLong(stats.getRouterNodes());
        os.writeLong(stats.getRouterLeaves());
        os.writeLong(stats.getRouterContacts());
        os.writeLong(stats.getTotalBytesReceived());
        os.writeLong(stats.getTotalBytesSent());
        os.writeLong(stats.getTotalPacketsReceived());
        os.writeLong(stats.getTotalPacketsSent());
        os.writeLong(stats.getTotalPingsReceived());
        os.writeLong(stats.getTotalFindNodesReceived());
        os.writeLong(stats.getTotalFindValuesReceived());
        os.writeLong(stats.getTotalStoresReceived());
        os.writeLong(stats.getAverageBytesReceived());
        os.writeLong(stats.getAverageBytesSent());
        os.writeLong(stats.getAveragePacketsReceived());
        os.writeLong(stats.getAveragePacketsSent());
        os.writeLong(stats.getIncomingRequests());
        String azversion = stats.getVersion() + "[" + version + "]";
        serialiseByteArray(os, azversion.getBytes(), 64);
        os.writeLong(stats.getRouterUptime());
        os.writeInt(stats.getRouterCount());
        if (version >= DHTTransportUDP.PROTOCOL_VERSION_BLOCK_KEYS) {
            os.writeLong(stats.getDBKeysBlocked());
            os.writeLong(stats.getTotalKeyBlocksReceived());
        }
        if (version >= DHTTransportUDP.PROTOCOL_VERSION_MORE_STATS) {
            os.writeLong(stats.getDBKeyCount());
            os.writeLong(stats.getDBValueCount());
            os.writeLong(stats.getDBStoreSize());
            os.writeLong(stats.getDBKeyDivFreqCount());
            os.writeLong(stats.getDBKeyDivSizeCount());
        }
    }

    protected static DHTTransportFullStats deserialiseStats(int version, DataInputStream is) throws IOException {
        final long db_values_stored = is.readLong();
        final long router_nodes = is.readLong();
        final long router_leaves = is.readLong();
        final long router_contacts = is.readLong();
        final long total_bytes_received = is.readLong();
        final long total_bytes_sent = is.readLong();
        final long total_packets_received = is.readLong();
        final long total_packets_sent = is.readLong();
        final long total_pings_received = is.readLong();
        final long total_find_nodes_received = is.readLong();
        final long total_find_values_received = is.readLong();
        final long total_stores_received = is.readLong();
        final long average_bytes_received = is.readLong();
        final long average_bytes_sent = is.readLong();
        final long average_packets_received = is.readLong();
        final long average_packets_sent = is.readLong();
        final long incoming_requests = is.readLong();
        final String az_version = new String(deserialiseByteArray(is, 64));
        final long router_uptime = is.readLong();
        final int router_count = is.readInt();
        final long db_keys_blocked;
        final long total_key_blocks_received;
        if (version >= DHTTransportUDP.PROTOCOL_VERSION_BLOCK_KEYS) {
            db_keys_blocked = is.readLong();
            total_key_blocks_received = is.readLong();
        } else {
            db_keys_blocked = -1;
            total_key_blocks_received = -1;
        }
        final long db_key_count;
        final long db_value_count;
        final long db_store_size;
        final long db_freq_divs;
        final long db_size_divs;
        if (version >= DHTTransportUDP.PROTOCOL_VERSION_MORE_STATS) {
            db_key_count = is.readLong();
            db_value_count = is.readLong();
            db_store_size = is.readLong();
            db_freq_divs = is.readLong();
            db_size_divs = is.readLong();
        } else {
            db_key_count = -1;
            db_value_count = -1;
            db_store_size = -1;
            db_freq_divs = -1;
            db_size_divs = -1;
        }
        DHTTransportFullStats res = new DHTTransportFullStats() {

            public long getDBValuesStored() {
                return (db_values_stored);
            }

            public long getDBKeysBlocked() {
                return (db_keys_blocked);
            }

            public long getDBValueCount() {
                return (db_value_count);
            }

            public long getDBKeyCount() {
                return (db_key_count);
            }

            public long getDBKeyDivSizeCount() {
                return (db_size_divs);
            }

            public long getDBKeyDivFreqCount() {
                return (db_freq_divs);
            }

            public long getDBStoreSize() {
                return (db_store_size);
            }

            public long getRouterNodes() {
                return (router_nodes);
            }

            public long getRouterLeaves() {
                return (router_leaves);
            }

            public long getRouterContacts() {
                return (router_contacts);
            }

            public long getRouterUptime() {
                return (router_uptime);
            }

            public int getRouterCount() {
                return (router_count);
            }

            public long getTotalBytesReceived() {
                return (total_bytes_received);
            }

            public long getTotalBytesSent() {
                return (total_bytes_sent);
            }

            public long getTotalPacketsReceived() {
                return (total_packets_received);
            }

            public long getTotalPacketsSent() {
                return (total_packets_sent);
            }

            public long getTotalPingsReceived() {
                return (total_pings_received);
            }

            public long getTotalFindNodesReceived() {
                return (total_find_nodes_received);
            }

            public long getTotalFindValuesReceived() {
                return (total_find_values_received);
            }

            public long getTotalStoresReceived() {
                return (total_stores_received);
            }

            public long getTotalKeyBlocksReceived() {
                return (total_key_blocks_received);
            }

            public long getAverageBytesReceived() {
                return (average_bytes_received);
            }

            public long getAverageBytesSent() {
                return (average_bytes_sent);
            }

            public long getAveragePacketsReceived() {
                return (average_packets_received);
            }

            public long getAveragePacketsSent() {
                return (average_packets_sent);
            }

            public long getIncomingRequests() {
                return (incoming_requests);
            }

            public String getVersion() {
                return (az_version);
            }

            public String getString() {
                return ("transport:" + getTotalBytesReceived() + "," + getTotalBytesSent() + "," + getTotalPacketsReceived() + "," + getTotalPacketsSent() + "," + getTotalPingsReceived() + "," + getTotalFindNodesReceived() + "," + getTotalFindValuesReceived() + "," + getTotalStoresReceived() + "," + getTotalKeyBlocksReceived() + "," + getAverageBytesReceived() + "," + getAverageBytesSent() + "," + getAveragePacketsReceived() + "," + getAveragePacketsSent() + "," + getIncomingRequests() + ",router:" + getRouterNodes() + "," + getRouterLeaves() + "," + getRouterContacts() + ",database:" + getDBKeyCount() + "," + getDBValueCount() + "," + getDBValuesStored() + "," + getDBStoreSize() + "," + getDBKeyDivFreqCount() + "," + getDBKeyDivSizeCount() + "," + getDBKeysBlocked() + ",version:" + getVersion() + "," + getRouterUptime() + "," + getRouterCount());
            }
        };
        return (res);
    }
}
