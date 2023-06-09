package com.burgess.rtd.constants;

public class Program {

    public static final String APPLICATION = "rtd";

    public static final String LOG = "rtd";

    public static class Config {

        public static final String AUTH_TOKEN = "authtoken";

        public static final String LAST_SYNC = "lastsync";

        public static final String USERNAME = "username";

        public static final String FULLNAME = "fullname";

        public static final String ID = "id";

        public static final String SYNC_TIME = "synctime";

        public static final String TIMELINE = "timeline";

        public static class SyncValues {

            public static final int HALF_HOUR = 0;

            public static final int HOUR = 1;

            public static final int TWO_HOURS = 2;

            public static final int TWICE_DAILY = 3;

            public static final int DAILY = 4;

            public static final int MANUALLY = 5;
        }
    }

    public static final String DEFAULT_AUTH_TOKEN = "0000000000000000";

    public static final Dates DATE_FORMAT = new Dates();

    public static class Dialog {

        public static final int GET_FROB = 1;

        public static final int GET_AUTH = 2;

        public static final int ERROR = 3;

        public static final int SYNCHRONIZE = 4;

        public static final int RENAME_LIST = 5;
    }

    public static class Error {

        public static final int EXCEPTION = 1;

        public static final int MALFORMED_URL = 2;

        public static final int IO_EXCEPTION = 3;

        public static final int HTTP_EXCEPTION = 4;

        public static final int NETWORK_UNAVAILABLE_EXCEPTION = 5;

        public static final int SQL_EXCEPTION = 6;

        public static final int JSON_EXCEPTION = 7;

        public static final int PARSE_EXCEPTION = 8;

        public static final int RTM_ERROR = 9;

        public static final int NOT_AUTHENTICATED = 10;
    }

    public static class Menu {

        public static final int CONFIGURE = 0;

        public static final int LISTS = 1;

        public static final int NEW_LIST = 2;

        public static final int VIEW_ARCHIVED = 3;

        public static final int NEW_TASK = 4;
    }

    public static class Request {

        public static final int SYNC = 0;
    }

    public static class Data {

        public static final int LIST = 0;

        public static final int LOCATION = 1;

        public static final int NOTE = 2;

        public static final int TAG = 3;

        public static final int TASK = 4;

        public static final int TASK_SERIES = 5;
    }
}
