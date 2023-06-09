package atnf.atoms.mon.comms;

public class DataValueRelTime extends DataValue {

    public DataValueRelTime() {
        super();
    }

    public DataValueRelTime(DataType type, long value) {
        super(type);
        this.value = value;
    }

    private static class __F implements Ice.ObjectFactory {

        public Ice.Object create(String type) {
            assert (type.equals(ice_staticId()));
            return new DataValueRelTime();
        }

        public void destroy() {
        }
    }

    private static Ice.ObjectFactory _factory = new __F();

    public static Ice.ObjectFactory ice_factory() {
        return _factory;
    }

    public static final String[] __ids = { "::Ice::Object", "::atnf::atoms::mon::comms::DataValue", "::atnf::atoms::mon::comms::DataValueRelTime" };

    public boolean ice_isA(String s) {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current) {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids() {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current) {
        return __ids;
    }

    public String ice_id() {
        return __ids[2];
    }

    public String ice_id(Ice.Current __current) {
        return __ids[2];
    }

    public static String ice_staticId() {
        return __ids[2];
    }

    public void __write(IceInternal.BasicStream __os) {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.writeLong(value);
        __os.endWriteSlice();
        super.__write(__os);
    }

    public void __read(IceInternal.BasicStream __is, boolean __rid) {
        if (__rid) {
            __is.readTypeId();
        }
        __is.startReadSlice();
        value = __is.readLong();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void __write(Ice.OutputStream __outS) {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type atnf::atoms::mon::comms::DataValueRelTime was not generated with stream support";
        throw ex;
    }

    public void __read(Ice.InputStream __inS, boolean __rid) {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type atnf::atoms::mon::comms::DataValueRelTime was not generated with stream support";
        throw ex;
    }

    public long value;
}
