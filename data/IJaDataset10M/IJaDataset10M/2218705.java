package ch.ethz.ssh2.packets;

import java.io.IOException;

/**
 * PacketUserauthBanner.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PacketUserauthFailure.java,v 1.3 2005/08/24 17:54:09 cplattne Exp $
 */
public class PacketUserauthFailure {

    byte[] payload;

    String[] authThatCanContinue;

    boolean partialSuccess;

    public PacketUserauthFailure(String[] authThatCanContinue, boolean partialSuccess) {
        this.authThatCanContinue = authThatCanContinue;
        this.partialSuccess = partialSuccess;
    }

    public PacketUserauthFailure(byte payload[], int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != Packets.SSH_MSG_USERAUTH_FAILURE) throw new IOException("This is not a SSH_MSG_USERAUTH_FAILURE! (" + packet_type + ")");
        authThatCanContinue = tr.readNameList();
        partialSuccess = tr.readBoolean();
        if (tr.remain() != 0) throw new IOException("Padding in SSH_MSG_USERAUTH_FAILURE packet!");
    }

    public String[] getAuthThatCanContinue() {
        return authThatCanContinue;
    }

    public boolean isPartialSuccess() {
        return partialSuccess;
    }
}
