package library.corba;

/**
* library/corba/IntTuple.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl/CORBALibrary.idl
* Monday, July 12, 2010 5:38:18 PM CEST
*/
public final class IntTuple implements org.omg.CORBA.portable.IDLEntity {

    public boolean isNull = false;

    public int key = (int) 0;

    public int value = (int) 0;

    public IntTuple() {
    }

    public IntTuple(boolean _isNull, int _key, int _value) {
        isNull = _isNull;
        key = _key;
        value = _value;
    }
}
