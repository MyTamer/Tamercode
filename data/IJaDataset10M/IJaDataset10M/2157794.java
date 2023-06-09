package com.db4o.internal.cs.messages;

import com.db4o.*;
import com.db4o.ext.*;
import com.db4o.foundation.*;
import com.db4o.foundation.network.*;
import com.db4o.internal.*;
import com.db4o.internal.cs.*;

/**
 * Messages for Client/Server Communication
 */
public abstract class Msg implements Cloneable {

    static int _messageIdGenerator = 1;

    private static Msg[] _messages = new Msg[70];

    int _msgID;

    String _name;

    private Transaction _trans;

    private MessageDispatcher _messageDispatcher;

    public static final MRuntimeException RUNTIME_EXCEPTION = new MRuntimeException();

    public static final MClassID CLASS_ID = new MClassID();

    public static final MClassMetadataIdForName CLASS_METADATA_ID_FOR_NAME = new MClassMetadataIdForName();

    public static final MClassNameForID CLASS_NAME_FOR_ID = new MClassNameForID();

    public static final MClose CLOSE = new MClose();

    public static final MCloseSocket CLOSE_SOCKET = new MCloseSocket();

    public static final MCommit COMMIT = new MCommit();

    public static final MCommittedCallBackRegistry COMMITTED_CALLBACK_REGISTER = new MCommittedCallBackRegistry();

    public static final MCommittedInfo COMMITTED_INFO = new MCommittedInfo();

    public static final MCommitSystemTransaction COMMIT_SYSTEMTRANS = new MCommitSystemTransaction();

    public static final MCreateClass CREATE_CLASS = new MCreateClass();

    public static final MClassMeta CLASS_META = new MClassMeta();

    public static final MVersion CURRENT_VERSION = new MVersion();

    public static final MDelete DELETE = new MDelete();

    public static final MError ERROR = new MError();

    public static final MFailed FAILED = new MFailed();

    public static final MGetAll GET_ALL = new MGetAll();

    public static final MGetClasses GET_CLASSES = new MGetClasses();

    public static final MGetInternalIDs GET_INTERNAL_IDS = new MGetInternalIDs();

    public static final MGetThreadID GET_THREAD_ID = new MGetThreadID();

    public static final MIDList ID_LIST = new MIDList();

    public static final MIdentity IDENTITY = new MIdentity();

    public static final MIsAlive IS_ALIVE = new MIsAlive();

    public static final MLength LENGTH = new MLength();

    public static final MLogin LOGIN = new MLogin();

    public static final MLoginOK LOGIN_OK = new MLoginOK();

    public static final MNull NULL = new MNull();

    public static final MObjectByUuid OBJECT_BY_UUID = new MObjectByUuid();

    public static final MsgObject OBJECT_TO_CLIENT = new MsgObject();

    public static final MObjectSetFetch OBJECTSET_FETCH = new MObjectSetFetch();

    public static final MObjectSetFinalized OBJECTSET_FINALIZED = new MObjectSetFinalized();

    public static final MObjectSetGetId OBJECTSET_GET_ID = new MObjectSetGetId();

    public static final MObjectSetIndexOf OBJECTSET_INDEXOF = new MObjectSetIndexOf();

    public static final MObjectSetReset OBJECTSET_RESET = new MObjectSetReset();

    public static final MObjectSetSize OBJECTSET_SIZE = new MObjectSetSize();

    public static final MOK OK = new MOK();

    public static final MPing PING = new MPing();

    public static final MPong PONG = new MPong();

    public static final MPrefetchIDs PREFETCH_IDS = new MPrefetchIDs();

    public static final MProcessDeletes PROCESS_DELETES = new MProcessDeletes();

    public static final MQueryExecute QUERY_EXECUTE = new MQueryExecute();

    public static final MQueryResult QUERY_RESULT = new MQueryResult();

    public static final MRaiseVersion RAISE_VERSION = new MRaiseVersion();

    public static final MReadBlob READ_BLOB = new MReadBlob();

    public static final MReadBytes READ_BYTES = new MReadBytes();

    public static final MReadMultipleObjects READ_MULTIPLE_OBJECTS = new MReadMultipleObjects();

    public static final MReadObject READ_OBJECT = new MReadObject();

    public static final MReleaseSemaphore RELEASE_SEMAPHORE = new MReleaseSemaphore();

    public static final MRollback ROLLBACK = new MRollback();

    public static final MSetSemaphore SET_SEMAPHORE = new MSetSemaphore();

    public static final MSuccess SUCCESS = new MSuccess();

    public static final MSwitchToFile SWITCH_TO_FILE = new MSwitchToFile();

    public static final MSwitchToMainFile SWITCH_TO_MAIN_FILE = new MSwitchToMainFile();

    public static final MTaDelete TA_DELETE = new MTaDelete();

    public static final MTaIsDeleted TA_IS_DELETED = new MTaIsDeleted();

    public static final MUserMessage USER_MESSAGE = new MUserMessage();

    public static final MUseTransaction USE_TRANSACTION = new MUseTransaction();

    public static final MWriteBlob WRITE_BLOB = new MWriteBlob();

    public static final MWriteNew WRITE_NEW = new MWriteNew();

    public static final MWriteUpdate WRITE_UPDATE = new MWriteUpdate();

    public static final MWriteUpdateDeleteMembers WRITE_UPDATE_DELETE_MEMBERS = new MWriteUpdateDeleteMembers();

    public static final MWriteBatchedMessages WRITE_BATCHED_MESSAGES = new MWriteBatchedMessages();

    public static final MsgBlob DELETE_BLOB_FILE = new MDeleteBlobFile();

    public static final MInstanceCount INSTANCE_COUNT = new MInstanceCount();

    Msg() {
        _msgID = _messageIdGenerator++;
        _messages[_msgID] = this;
    }

    Msg(String aName) {
        this();
        _name = aName;
    }

    public static Msg getMessage(int id) {
        return _messages[id];
    }

    public final Msg publicClone() {
        try {
            return (Msg) clone();
        } catch (CloneNotSupportedException e) {
            Exceptions4.shouldNeverHappen();
            return null;
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return _msgID == ((Msg) obj)._msgID;
    }

    public int hashCode() {
        return _msgID;
    }

    /**
	 * dummy method to allow clean override handling
	 * without casting
	 */
    public ByteArrayBuffer getByteLoad() {
        return null;
    }

    final String getName() {
        if (_name == null) {
            return getClass().getName();
        }
        return _name;
    }

    protected LocalTransaction serverTransaction() {
        return (LocalTransaction) _trans;
    }

    protected Transaction transaction() {
        return _trans;
    }

    protected LocalObjectContainer file() {
        return (LocalObjectContainer) stream();
    }

    protected ObjectContainerBase stream() {
        return transaction().container();
    }

    protected Object streamLock() {
        return stream().lock();
    }

    protected Config4Impl config() {
        return stream().config();
    }

    protected static StatefulBuffer readMessageBuffer(Transaction trans, Socket4 sock) throws Db4oIOException {
        return readMessageBuffer(trans, sock, Const4.MESSAGE_LENGTH);
    }

    protected static StatefulBuffer readMessageBuffer(Transaction trans, Socket4 sock, int length) throws Db4oIOException {
        StatefulBuffer buffer = new StatefulBuffer(trans, length);
        int offset = 0;
        while (length > 0) {
            int read = sock.read(buffer._buffer, offset, length);
            if (read < 0) {
                throw new Db4oIOException();
            }
            offset += read;
            length -= read;
        }
        return buffer;
    }

    public static final Msg readMessage(MessageDispatcher messageDispatcher, Transaction trans, Socket4 sock) throws Db4oIOException {
        StatefulBuffer reader = readMessageBuffer(trans, sock);
        Msg message = _messages[reader.readInt()].readPayLoad(messageDispatcher, trans, sock, reader);
        if (Debug.messages) {
            System.out.println(message + " arrived at " + trans.container());
        }
        return message;
    }

    /** @param sock */
    Msg readPayLoad(MessageDispatcher messageDispatcher, Transaction a_trans, Socket4 sock, ByteArrayBuffer reader) {
        Msg msg = publicClone();
        msg.setMessageDispatcher(messageDispatcher);
        msg.setTransaction(checkParentTransaction(a_trans, reader));
        return msg;
    }

    protected final Transaction checkParentTransaction(Transaction a_trans, ByteArrayBuffer reader) {
        if (reader.readByte() == Const4.SYSTEM_TRANS && a_trans.parentTransaction() != null) {
            return a_trans.parentTransaction();
        }
        return a_trans;
    }

    public final void setTransaction(Transaction aTrans) {
        _trans = aTrans;
    }

    public final String toString() {
        return getName();
    }

    public void write(Msg msg) {
        _messageDispatcher.write(msg);
    }

    public void writeException(RuntimeException e) {
        write(RUNTIME_EXCEPTION.getWriterForSingleObject(transaction(), e));
    }

    public void respondInt(int response) {
        write(ID_LIST.getWriterForInt(transaction(), response));
    }

    public boolean write(Socket4 sock) {
        if (null == sock) {
            throw new ArgumentNullException();
        }
        synchronized (sock) {
            try {
                if (Debug.messages) {
                    System.out.println(this + " sent by " + Thread.currentThread().getName());
                }
                sock.write(payLoad()._buffer);
                sock.flush();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public StatefulBuffer payLoad() {
        StatefulBuffer writer = new StatefulBuffer(transaction(), Const4.MESSAGE_LENGTH);
        writer.writeInt(_msgID);
        return writer;
    }

    public MessageDispatcher messageDispatcher() {
        return _messageDispatcher;
    }

    public ServerMessageDispatcher serverMessageDispatcher() {
        if (_messageDispatcher instanceof ServerMessageDispatcher) {
            return (ServerMessageDispatcher) _messageDispatcher;
        }
        throw new IllegalStateException();
    }

    public ClientMessageDispatcher clientMessageDispatcher() {
        if (_messageDispatcher instanceof ClientMessageDispatcher) {
            return (ClientMessageDispatcher) _messageDispatcher;
        }
        throw new IllegalStateException();
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        _messageDispatcher = messageDispatcher;
    }

    public void logMsg(int msgCode, String msg) {
        stream().logMsg(msgCode, msg);
    }
}
