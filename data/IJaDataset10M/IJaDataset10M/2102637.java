package org.ogre4j;

import org.xbig.base.*;

public interface IResourceBackgroundQueue extends INativeObject, org.ogre4j.IResourceAllocatedObject, org.ogre4j.ISingleton<org.ogre4j.IResourceBackgroundQueue> {

    public static interface IListener extends INativeObject {

        /** 
    Called when a requested operation completes, queued into main thread. **/
        public void operationCompleted(long ticket, org.ogre4j.IBackgroundProcessResult result);

        /** 
    Called when a requested operation completes, immediate in background thread. **/
        public void operationCompletedInThread(long ticket, org.ogre4j.IBackgroundProcessResult result);
    }

    public static interface IQueuedNotification extends INativeObject {

        /** **/
        public boolean getload();

        /** **/
        public void setload(boolean _jni_value_);

        /** **/
        public org.ogre4j.IResource getresource();

        /** **/
        public void setresource(org.ogre4j.IResource _jni_value_);

        /** **/
        public void getreq(org.ogre4j.IResourceBackgroundQueue.IRequest returnValue);

        /** **/
        public void setreq(org.ogre4j.IResourceBackgroundQueue.IRequest _jni_value_);
    }

    public static interface IRequest extends INativeObject {

        /** **/
        public long getticketID();

        /** **/
        public void setticketID(long _jni_value_);

        /** **/
        public org.ogre4j.ResourceBackgroundQueue.RequestType gettype();

        /** **/
        public void settype(org.ogre4j.ResourceBackgroundQueue.RequestType _jni_value_);

        /** **/
        public String getresourceName();

        /** **/
        public void setresourceName(String _jni_value_);

        /** **/
        public long getresourceHandle();

        /** **/
        public void setresourceHandle(long _jni_value_);

        /** **/
        public String getresourceType();

        /** **/
        public void setresourceType(String _jni_value_);

        /** **/
        public String getgroupName();

        /** **/
        public void setgroupName(String _jni_value_);

        /** **/
        public boolean getisManual();

        /** **/
        public void setisManual(boolean _jni_value_);

        /** **/
        public org.ogre4j.IManualResourceLoader getloader();

        /** **/
        public void setloader(org.ogre4j.IManualResourceLoader _jni_value_);

        /** **/
        public org.ogre4j.INameValuePairList getloadParams();

        /** **/
        public org.ogre4j.IResourceBackgroundQueue.IListener getlistener();

        /** **/
        public void setlistener(org.ogre4j.IResourceBackgroundQueue.IListener _jni_value_);

        /** **/
        public void getresult(org.ogre4j.IBackgroundProcessResult returnValue);

        /** **/
        public void setresult(org.ogre4j.IBackgroundProcessResult _jni_value_);
    }

    public interface IRequestQueue extends INativeObject, org.std.Ilist<org.ogre4j.IResourceBackgroundQueue.IRequest> {

        /** **/
        public void assign(int num, org.ogre4j.IResourceBackgroundQueue.IRequest val);

        /** **/
        public org.ogre4j.IResourceBackgroundQueue.IRequest back();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.IResourceBackgroundQueue.IRequest front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void pop_front();

        /** **/
        public void push_back(org.ogre4j.IResourceBackgroundQueue.IRequest val);

        /** **/
        public void push_front(org.ogre4j.IResourceBackgroundQueue.IRequest val);

        /** **/
        public void remove(org.ogre4j.IResourceBackgroundQueue.IRequest val);

        /** **/
        public void reverse();

        /** **/
        public int size();

        /** **/
        public void unique();
    }

    public interface IRequestTicketMap extends INativeObject, org.std.Imap<Long, org.ogre4j.IResourceBackgroundQueue.IRequest> {

        /** **/
        public void clear();

        /** **/
        public int count(long key);

        /** **/
        public boolean empty();

        /** **/
        public int erase(long key);

        /** **/
        public int max_size();

        /** **/
        public int size();

        /** **/
        public org.ogre4j.IResourceBackgroundQueue.IRequest get(long key);

        /** **/
        public void insert(long key, org.ogre4j.IResourceBackgroundQueue.IRequest value);
    }

    public interface INotificationQueue extends INativeObject, org.std.Ilist<org.ogre4j.IResourceBackgroundQueue.IQueuedNotification> {

        /** **/
        public void assign(int num, org.ogre4j.IResourceBackgroundQueue.IQueuedNotification val);

        /** **/
        public org.ogre4j.IResourceBackgroundQueue.IQueuedNotification back();

        /** **/
        public void clear();

        /** **/
        public boolean empty();

        /** **/
        public org.ogre4j.IResourceBackgroundQueue.IQueuedNotification front();

        /** **/
        public int max_size();

        /** **/
        public void pop_back();

        /** **/
        public void pop_front();

        /** **/
        public void push_back(org.ogre4j.IResourceBackgroundQueue.IQueuedNotification val);

        /** **/
        public void push_front(org.ogre4j.IResourceBackgroundQueue.IQueuedNotification val);

        /** **/
        public void remove(org.ogre4j.IResourceBackgroundQueue.IQueuedNotification val);

        /** **/
        public void reverse();

        /** **/
        public int size();

        /** **/
        public void unique();
    }

    /** 
    Sets whether or not a thread should be created and started to handle the background loading, or whether a user thread will call the appropriate hooks. **/
    public void setStartBackgroundThread(boolean startThread);

    /** 
    Gets whether or not a thread should be created and started to handle the background loading, or whether a user thread will call the appropriate hooks. **/
    public boolean getStartBackgroundThread();

    /** 
    Initialise the background queue system. **/
    public void initialise();

    /** 
    Shut down the background queue system. **/
    public void shutdown();

    /** 
    Initialise a resource group in the background. **/
    public long initialiseResourceGroup(String name, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Initialise all resource groups which are yet to be initialised in the background. **/
    public long initialiseAllResourceGroups(org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Prepares a resource group in the background. **/
    public long prepareResourceGroup(String name, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Loads a resource group in the background. **/
    public long loadResourceGroup(String name, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Unload a single resource in the background. **/
    public long unload(String resType, String name, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Unload a single resource in the background. **/
    public long unload(String resType, long handle, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Unloads a resource group in the background. **/
    public long unloadResourceGroup(String name, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Prepare a single resource in the background. **/
    public long prepare(String resType, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList loadParams, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Load a single resource in the background. **/
    public long load(String resType, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList loadParams, org.ogre4j.IResourceBackgroundQueue.IListener listener);

    /** 
    Returns whether a previously queued process has completed or not. **/
    public boolean isProcessComplete(long ticket);

    /** 
    Process a single queued background operation. **/
    public boolean _doNextQueuedBackgroundProcess();

    /** 
    Initialise processing for a background thread. **/
    public void _initThread();

    /** 
    Queue the firing of the 'background preparing complete' event to a  event. **/
    public void _queueFireBackgroundPreparingComplete(org.ogre4j.IResource res);

    /** 
    Queue the firing of the 'background loading complete' event to a  event. **/
    public void _queueFireBackgroundLoadingComplete(org.ogre4j.IResource res);

    /** 
    Fires all the queued events for background loaded resources. **/
    public void _fireOnFrameCallbacks();
}
