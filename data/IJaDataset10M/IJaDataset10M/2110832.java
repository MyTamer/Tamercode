package net.minecraft.src;

import java.io.*;

public class Packet22Collect extends Packet {

    /** The entity on the ground that was picked up. */
    public int collectedEntityId;

    /** The entity that picked up the one from the ground. */
    public int collectorEntityId;

    public Packet22Collect() {
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
        collectedEntityId = par1DataInputStream.readInt();
        collectorEntityId = par1DataInputStream.readInt();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
        par1DataOutputStream.writeInt(collectedEntityId);
        par1DataOutputStream.writeInt(collectorEntityId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler) {
        par1NetHandler.handleCollect(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize() {
        return 8;
    }
}
