// instantiated with jti.pl from EventDispatcher

/****************************************************************************
  Copyright 2001-2018 Sphenon GmbH

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
*****************************************************************************/
// please do not modify this file directly
package com.sphenon.basics.operations.tplinst;

import com.sphenon.basics.operations.*;
import com.sphenon.basics.event.*;

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.event.*;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class EventDispatcher_ActivityEvent_
    implements EventListener_ActivityEvent_
{
    static final public Class _class = EventDispatcher_ActivityEvent_.class;

    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(_class); };

    private WeakHashMap<EventListener_ActivityEvent_,Long> listeners;
    private long processcount = 0L;
    private String id;

    public EventDispatcher_ActivityEvent_ (CallContext context) {
        this.listeners = null;
        this.id = null;
    }

    public EventDispatcher_ActivityEvent_ (CallContext context, String id) {
        this.listeners = null;
        this.id = id;
    }

    protected WeakHashMap<EventListener_ActivityEvent_,Long> getListeners(CallContext context) {
        if (this.listeners == null) {
            listeners = new WeakHashMap<EventListener_ActivityEvent_,Long>();
        }
        return this.listeners;
    }

    public boolean hasListeners(CallContext context) {
        return (this.listeners != null && this.listeners.size() != 0);
    }

    public void addListener(CallContext context, EventListener_ActivityEvent_ listener) {
        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Dispatcher '%(id)', adding listener '%(listener)'", "id", this.id, "listener", listener); }
        if (this.isInProcess()) {
            this.createCopyOfListeners();
        }
        this.getListeners(context).put(listener, 1L);
    }

    public void removeListener(CallContext context, EventListener_ActivityEvent_ listener) {
        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Dispatcher '%(id)', removing listener '%(listener)'", "id", this.id, "listener", listener); }
        if (this.isInProcess()) {
            this.createCopyOfListeners();
        }   
        this.getListeners(context).remove(listener);
    }

    private void beginProcess(){
        this.processcount++;
    }
  
    private void endProcess(){
        this.processcount--;
    }

    private boolean isInProcess(){
        return this.processcount > 0 ? true : false;
    }

    private void createCopyOfListeners(){
        WeakHashMap<EventListener_ActivityEvent_,Long> hash = new WeakHashMap<EventListener_ActivityEvent_,Long>();       
        hash.putAll( this.listeners );
        this.listeners = hash;
    } 

    public void notify(CallContext call_context, ActivityEvent event) {
        Context context = (Context) call_context;
        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Dispatcher '%(id)' received '%(event)'", "id", this.id, "event", event); }
        WeakHashMap<EventListener_ActivityEvent_,Long> local_listeners = this.getListeners(context);
        this.beginProcess();
        for (EventListener_ActivityEvent_ elet : local_listeners.keySet()) {
            if (elet != null) {
                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Dispatching event '%(event)' from '%(id)' to '%(listener)'", "event", event, "id", this.id, "listener", elet); }
                elet.notify(call_context, event);
            } else {
                CustomaryContext cc = CustomaryContext.create(context);
                cc.sendNotice(context,EventStringPool.get(context, "0.0.0" /* EventListener vanished */));
            }
        }
        this.endProcess();
    }

    public void notify(CallContext call_context) {
        Context context = (Context) call_context;
        if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Dispatcher '%(id)' received '_notify_'", "id", this.id); }
        WeakHashMap<EventListener_ActivityEvent_,Long> local_listeners = this.getListeners(context);
        this.beginProcess();
        for (EventListener_ActivityEvent_ elet : local_listeners.keySet()) {
            if (elet != null) {
                if ((notification_level & Notifier.DIAGNOSTICS) != 0) { NotificationContext.sendDiagnostics(context, "Dispatching event '_notify_' from '%(id)' to '%(listener)'", "id", this.id, "listener", elet); }
                elet.notify(call_context);
            } else {
                CustomaryContext cc = CustomaryContext.create(context);
                cc.sendNotice(context,EventStringPool.get(context, "0.0.1" /* EventListener vanished */));
            }
        }
        this.endProcess();
    }

    public String toString() {
        return super.toString() + "[" + this.id + "]";
    }
}
