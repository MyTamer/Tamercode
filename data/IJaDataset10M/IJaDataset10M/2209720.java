package com.android.bluetooth.pbap;

import com.android.bluetooth.R;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.database.Cursor;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.PhoneLookup;
import android.pim.vcard.VCardComposer;
import android.pim.vcard.VCardConfig;
import android.pim.vcard.VCardComposer.OneEntryHandler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import javax.obex.ResponseCodes;
import javax.obex.Operation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

public class BluetoothPbapVcardManager {

    private static final String TAG = "BluetoothPbapVcardManager";

    private static final boolean V = BluetoothPbapService.VERBOSE;

    private ContentResolver mResolver;

    private Context mContext;

    private StringBuilder mVcardResults = null;

    static final String[] PHONES_PROJECTION = new String[] { Data._ID, CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.LABEL, CommonDataKinds.Phone.NUMBER, Contacts.DISPLAY_NAME };

    private static final int ID_COLUMN_INDEX = 0;

    private static final int PHONE_TYPE_COLUMN_INDEX = 1;

    private static final int PHONE_LABEL_COLUMN_INDEX = 2;

    private static final int PHOEN_NUMBER_COLUMN_INDEX = 3;

    private static final int CONTACTS_DISPLAY_NAME_COLUMN_INDEX = 4;

    static final String SORT_ORDER_PHONE_NUMBER = CommonDataKinds.Phone.NUMBER + " ASC";

    static final String[] CONTACTS_PROJECTION = new String[] { Contacts._ID, Contacts.DISPLAY_NAME };

    static final int CONTACTS_ID_COLUMN_INDEX = 0;

    static final int CONTACTS_NAME_COLUMN_INDEX = 1;

    static final String CALLLOG_SORT_ORDER = Calls._ID + " DESC";

    private static final String CLAUSE_ONLY_VISIBLE = Contacts.IN_VISIBLE_GROUP + "=1";

    public BluetoothPbapVcardManager(final Context context) {
        mContext = context;
        mResolver = mContext.getContentResolver();
    }

    public final String getOwnerPhoneNumberVcard(final boolean vcardType21) {
        VCardComposer composer = new VCardComposer(mContext);
        String name = BluetoothPbapService.getLocalPhoneName();
        String number = BluetoothPbapService.getLocalPhoneNum();
        String vcard = composer.composeVCardForPhoneOwnNumber(Phone.TYPE_MOBILE, name, number, vcardType21);
        return vcard;
    }

    public final int getPhonebookSize(final int type) {
        int size;
        switch(type) {
            case BluetoothPbapObexServer.ContentType.PHONEBOOK:
                size = getContactsSize();
                break;
            default:
                size = getCallHistorySize(type);
                break;
        }
        if (V) Log.v(TAG, "getPhonebookSzie size = " + size + " type = " + type);
        return size;
    }

    public final int getContactsSize() {
        final Uri myUri = Contacts.CONTENT_URI;
        int size = 0;
        Cursor contactCursor = null;
        try {
            contactCursor = mResolver.query(myUri, null, CLAUSE_ONLY_VISIBLE, null, null);
            if (contactCursor != null) {
                size = contactCursor.getCount() + 1;
            }
        } finally {
            if (contactCursor != null) {
                contactCursor.close();
            }
        }
        return size;
    }

    public final int getCallHistorySize(final int type) {
        final Uri myUri = CallLog.Calls.CONTENT_URI;
        String selection = BluetoothPbapObexServer.createSelectionPara(type);
        int size = 0;
        Cursor callCursor = null;
        try {
            callCursor = mResolver.query(myUri, null, selection, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            if (callCursor != null) {
                size = callCursor.getCount();
            }
        } finally {
            if (callCursor != null) {
                callCursor.close();
            }
        }
        return size;
    }

    public final ArrayList<String> loadCallHistoryList(final int type) {
        final Uri myUri = CallLog.Calls.CONTENT_URI;
        String selection = BluetoothPbapObexServer.createSelectionPara(type);
        String[] projection = new String[] { Calls.NUMBER, Calls.CACHED_NAME };
        final int CALLS_NUMBER_COLUMN_INDEX = 0;
        final int CALLS_NAME_COLUMN_INDEX = 1;
        Cursor callCursor = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            callCursor = mResolver.query(myUri, projection, selection, null, CALLLOG_SORT_ORDER);
            if (callCursor != null) {
                for (callCursor.moveToFirst(); !callCursor.isAfterLast(); callCursor.moveToNext()) {
                    String name = callCursor.getString(CALLS_NAME_COLUMN_INDEX);
                    if (TextUtils.isEmpty(name)) {
                        name = callCursor.getString(CALLS_NUMBER_COLUMN_INDEX);
                    }
                    list.add(name);
                }
            }
        } finally {
            if (callCursor != null) {
                callCursor.close();
            }
        }
        return list;
    }

    public final ArrayList<String> getPhonebookNameList(final int orderByWhat) {
        ArrayList<String> nameList = new ArrayList<String>();
        nameList.add(BluetoothPbapService.getLocalPhoneName());
        final Uri myUri = Contacts.CONTENT_URI;
        Cursor contactCursor = null;
        try {
            if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_INDEXED) {
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE, null, Contacts._ID);
            } else if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_ALPHABETICAL) {
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE, null, Contacts.DISPLAY_NAME);
            }
            if (contactCursor != null) {
                for (contactCursor.moveToFirst(); !contactCursor.isAfterLast(); contactCursor.moveToNext()) {
                    String name = contactCursor.getString(CONTACTS_NAME_COLUMN_INDEX);
                    if (TextUtils.isEmpty(name)) {
                        name = mContext.getString(android.R.string.unknownName);
                    }
                    nameList.add(name);
                }
            }
        } finally {
            if (contactCursor != null) {
                contactCursor.close();
            }
        }
        return nameList;
    }

    public final ArrayList<String> getPhonebookNumberList() {
        ArrayList<String> numberList = new ArrayList<String>();
        numberList.add(BluetoothPbapService.getLocalPhoneNum());
        final Uri myUri = Phone.CONTENT_URI;
        Cursor phoneCursor = null;
        try {
            phoneCursor = mResolver.query(myUri, PHONES_PROJECTION, CLAUSE_ONLY_VISIBLE, null, SORT_ORDER_PHONE_NUMBER);
            if (phoneCursor != null) {
                for (phoneCursor.moveToFirst(); !phoneCursor.isAfterLast(); phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(PHOEN_NUMBER_COLUMN_INDEX);
                    if (TextUtils.isEmpty(number)) {
                        number = mContext.getString(R.string.defaultnumber);
                    }
                    numberList.add(number);
                }
            }
        } finally {
            if (phoneCursor != null) {
                phoneCursor.close();
            }
        }
        return numberList;
    }

    public final int composeAndSendCallLogVcards(final int type, final Operation op, final int startPoint, final int endPoint, final boolean vcardType21) {
        if (startPoint < 1 || startPoint > endPoint) {
            Log.e(TAG, "internal error: startPoint or endPoint is not correct.");
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        String typeSelection = BluetoothPbapObexServer.createSelectionPara(type);
        final Uri myUri = CallLog.Calls.CONTENT_URI;
        final String[] CALLLOG_PROJECTION = new String[] { CallLog.Calls._ID };
        final int ID_COLUMN_INDEX = 0;
        Cursor callsCursor = null;
        long startPointId = 0;
        long endPointId = 0;
        try {
            callsCursor = mResolver.query(myUri, CALLLOG_PROJECTION, typeSelection, null, CALLLOG_SORT_ORDER);
            if (callsCursor != null) {
                callsCursor.moveToPosition(startPoint - 1);
                startPointId = callsCursor.getLong(ID_COLUMN_INDEX);
                if (V) Log.v(TAG, "Call Log query startPointId = " + startPointId);
                if (startPoint == endPoint) {
                    endPointId = startPointId;
                } else {
                    callsCursor.moveToPosition(endPoint - 1);
                    endPointId = callsCursor.getLong(ID_COLUMN_INDEX);
                }
                if (V) Log.v(TAG, "Call log query endPointId = " + endPointId);
            }
        } finally {
            if (callsCursor != null) {
                callsCursor.close();
            }
        }
        String recordSelection;
        if (startPoint == endPoint) {
            recordSelection = Calls._ID + "=" + startPointId;
        } else {
            recordSelection = Calls._ID + ">=" + endPointId + " AND " + Calls._ID + "<=" + startPointId;
        }
        String selection;
        if (typeSelection == null) {
            selection = recordSelection;
        } else {
            selection = "(" + typeSelection + ") AND (" + recordSelection + ")";
        }
        if (V) Log.v(TAG, "Call log query selection is: " + selection);
        return composeAndSendVCards(op, selection, vcardType21, null, false);
    }

    public final int composeAndSendPhonebookVcards(final Operation op, final int startPoint, final int endPoint, final boolean vcardType21, String ownerVCard) {
        if (startPoint < 1 || startPoint > endPoint) {
            Log.e(TAG, "internal error: startPoint or endPoint is not correct.");
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        final Uri myUri = Contacts.CONTENT_URI;
        Cursor contactCursor = null;
        long startPointId = 0;
        long endPointId = 0;
        try {
            contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE, null, Contacts._ID);
            if (contactCursor != null) {
                contactCursor.moveToPosition(startPoint - 1);
                startPointId = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
                if (V) Log.v(TAG, "Query startPointId = " + startPointId);
                if (startPoint == endPoint) {
                    endPointId = startPointId;
                } else {
                    contactCursor.moveToPosition(endPoint - 1);
                    endPointId = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
                }
                if (V) Log.v(TAG, "Query endPointId = " + endPointId);
            }
        } finally {
            if (contactCursor != null) {
                contactCursor.close();
            }
        }
        final String selection;
        if (startPoint == endPoint) {
            selection = Contacts._ID + "=" + startPointId + " AND " + CLAUSE_ONLY_VISIBLE;
        } else {
            selection = Contacts._ID + ">=" + startPointId + " AND " + Contacts._ID + "<=" + endPointId + " AND " + CLAUSE_ONLY_VISIBLE;
        }
        if (V) Log.v(TAG, "Query selection is: " + selection);
        return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
    }

    public final int composeAndSendPhonebookOneVcard(final Operation op, final int offset, final boolean vcardType21, String ownerVCard, int orderByWhat) {
        if (offset < 1) {
            Log.e(TAG, "Internal error: offset is not correct.");
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        final Uri myUri = Contacts.CONTENT_URI;
        Cursor contactCursor = null;
        String selection = null;
        long contactId = 0;
        if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_INDEXED) {
            try {
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE, null, Contacts._ID);
                if (contactCursor != null) {
                    contactCursor.moveToPosition(offset - 1);
                    contactId = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
                    if (V) Log.v(TAG, "Query startPointId = " + contactId);
                }
            } finally {
                if (contactCursor != null) {
                    contactCursor.close();
                }
            }
        } else if (orderByWhat == BluetoothPbapObexServer.ORDER_BY_ALPHABETICAL) {
            try {
                contactCursor = mResolver.query(myUri, CONTACTS_PROJECTION, CLAUSE_ONLY_VISIBLE, null, Contacts.DISPLAY_NAME);
                if (contactCursor != null) {
                    contactCursor.moveToPosition(offset - 1);
                    contactId = contactCursor.getLong(CONTACTS_ID_COLUMN_INDEX);
                    if (V) Log.v(TAG, "Query startPointId = " + contactId);
                }
            } finally {
                if (contactCursor != null) {
                    contactCursor.close();
                }
            }
        } else {
            Log.e(TAG, "Parameter orderByWhat is not supported!");
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        selection = Contacts._ID + "=" + contactId;
        if (V) Log.v(TAG, "Query selection is: " + selection);
        return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
    }

    public final int composeAndSendVCards(final Operation op, final String selection, final boolean vcardType21, String ownerVCard, boolean isContacts) {
        long timestamp = 0;
        if (V) timestamp = System.currentTimeMillis();
        VCardComposer composer = null;
        try {
            final int vcardType;
            if (vcardType21) {
                vcardType = VCardConfig.VCARD_TYPE_V21_GENERIC;
            } else {
                vcardType = VCardConfig.VCARD_TYPE_V30_GENERIC;
            }
            final boolean careHandlerErrors = true;
            final boolean needPhoto = false;
            final boolean isCallLogComposer = !isContacts;
            composer = new VCardComposer(mContext, vcardType, careHandlerErrors, isCallLogComposer, needPhoto);
            composer.addHandler(new HandlerForStringBuffer(op, ownerVCard));
            if (!composer.init(selection, null)) {
                return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
            }
            while (!composer.isAfterLast()) {
                if (!composer.createOneEntry()) {
                    Log.e(TAG, "Failed to read a contact. Error reason: " + composer.getErrorReason());
                    return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
                }
            }
        } finally {
            if (composer != null) {
                composer.terminate();
            }
        }
        if (V) Log.v(TAG, "Total vcard composing and sending out takes " + (System.currentTimeMillis() - timestamp) + " ms");
        return ResponseCodes.OBEX_HTTP_OK;
    }

    /**
     * Handler to emit VCard String to PCE once size grow to maxPacketSize.
     */
    public class HandlerForStringBuffer implements OneEntryHandler {

        @SuppressWarnings("hiding")
        private Operation operation;

        private OutputStream outputStream;

        private int maxPacketSize;

        private String phoneOwnVCard = null;

        public HandlerForStringBuffer(Operation op, String ownerVCard) {
            operation = op;
            maxPacketSize = operation.getMaxPacketSize();
            if (V) Log.v(TAG, "getMaxPacketSize() = " + maxPacketSize);
            if (ownerVCard != null) {
                phoneOwnVCard = ownerVCard;
                if (V) Log.v(TAG, "phone own number vcard:");
                if (V) Log.v(TAG, phoneOwnVCard);
            }
        }

        public boolean onInit(Context context) {
            try {
                outputStream = operation.openOutputStream();
                mVcardResults = new StringBuilder();
                if (phoneOwnVCard != null) {
                    mVcardResults.append(phoneOwnVCard);
                }
            } catch (IOException e) {
                Log.e(TAG, "open outputstrem failed" + e.toString());
                return false;
            }
            if (V) Log.v(TAG, "openOutputStream() ok.");
            return true;
        }

        public boolean onEntryCreated(String vcard) {
            int vcardLen = vcard.length();
            if (V) Log.v(TAG, "The length of this vcard is: " + vcardLen);
            mVcardResults.append(vcard);
            int vcardStringLen = mVcardResults.toString().length();
            if (V) Log.v(TAG, "The length of this vcardResults is: " + vcardStringLen);
            if (vcardStringLen >= maxPacketSize) {
                long timestamp = 0;
                int position = 0;
                while (position < (vcardStringLen - maxPacketSize)) {
                    if (V) timestamp = System.currentTimeMillis();
                    String subStr = mVcardResults.toString().substring(position, position + maxPacketSize);
                    try {
                        outputStream.write(subStr.getBytes(), 0, maxPacketSize);
                    } catch (IOException e) {
                        Log.e(TAG, "write outputstrem failed" + e.toString());
                        return false;
                    }
                    if (V) Log.v(TAG, "Sending vcard String " + maxPacketSize + " bytes took " + (System.currentTimeMillis() - timestamp) + " ms");
                    position += maxPacketSize;
                }
                mVcardResults.delete(0, position);
            }
            return true;
        }

        public void onTerminate() {
            String lastStr = mVcardResults.toString();
            try {
                outputStream.write(lastStr.getBytes(), 0, lastStr.length());
            } catch (IOException e) {
                Log.e(TAG, "write outputstrem failed" + e.toString());
            }
            if (V) Log.v(TAG, "Last packet sent out, sending process complete!");
            if (!BluetoothPbapObexServer.closeStream(outputStream, operation)) {
                if (V) Log.v(TAG, "CloseStream failed!");
            } else {
                if (V) Log.v(TAG, "CloseStream ok!");
            }
        }
    }
}
