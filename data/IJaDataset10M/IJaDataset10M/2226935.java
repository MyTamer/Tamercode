package org.jdiameter.server.impl.app.s6a;

import org.jdiameter.api.InternalException;
import org.jdiameter.api.app.AppEvent;
import org.jdiameter.api.app.StateEvent;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class Event implements StateEvent {

    enum Type {

        SEND_MESSAGE, TIMEOUT_EXPIRES, RECEIVE_AIR, RECEIVE_PUR, RECEIVE_ULR, RECEIVE_NOR, RECEIVE_CLA, RECEIVE_IDA, RECEIVE_DSA, RECEIVE_RSA
    }

    AppEvent request;

    AppEvent answer;

    Type type;

    Event(Type type, AppEvent request, AppEvent answer) {
        this.type = type;
        this.answer = answer;
        this.request = request;
    }

    public <E> E encodeType(Class<E> eClass) {
        return eClass == Type.class ? (E) type : null;
    }

    public Enum getType() {
        return type;
    }

    public AppEvent getRequest() {
        return request;
    }

    public AppEvent getAnswer() {
        return answer;
    }

    public int compareTo(Object o) {
        return 0;
    }

    public Object getData() {
        return request != null ? request : answer;
    }

    public void setData(Object data) {
        try {
            if (((AppEvent) data).getMessage().isRequest()) {
                request = (AppEvent) data;
            } else {
                answer = (AppEvent) data;
            }
        } catch (InternalException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
