package fireteam.orb.server.stub;

/**
* fireteam/orb/server/stub/SignedData.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from FTDServer.idl
* 26 Август 2008 г. 15:19:19 MSD
*/
public final class SignedData implements org.omg.CORBA.portable.IDLEntity {

    public byte Data[] = null;

    public byte Sign[] = null;

    public SignedData() {
    }

    public SignedData(byte[] _Data, byte[] _Sign) {
        Data = _Data;
        Sign = _Sign;
    }
}
