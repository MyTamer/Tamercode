    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "connect_push_supplier", "disconnect_push_consumer", "get_typed_consumer", "push" };
        int _ob_left = 0;
        int _ob_right = _ob_names.length;
        int _ob_index = -1;
        while (_ob_left < _ob_right) {
            int _ob_m = (_ob_left + _ob_right) / 2;
            int _ob_res = _ob_names[_ob_m].compareTo(opName);
            if (_ob_res == 0) {
                _ob_index = _ob_m;
                break;
            } else if (_ob_res > 0) _ob_right = _ob_m; else _ob_left = _ob_m + 1;
        }
        switch(_ob_index) {
            case 0:
                return _OB_op_connect_push_supplier(in, handler);
            case 1:
                return _OB_op_disconnect_push_consumer(in, handler);
            case 2:
                return _OB_op_get_typed_consumer(in, handler);
            case 3:
                return _OB_op_push(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }