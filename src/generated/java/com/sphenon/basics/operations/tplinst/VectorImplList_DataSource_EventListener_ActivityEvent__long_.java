// instantiated with jti.pl from VectorImplList_DataSource

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

import com.sphenon.basics.context.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.many.*;
import com.sphenon.basics.many.tplinst.*;
import com.sphenon.basics.event.tplinst.EventDispatcher_ChangeEvent_;
import com.sphenon.basics.event.Changing;

// java
import java.util.Date;

import com.sphenon.basics.many.returncodes.*;

public class VectorImplList_DataSource_EventListener_ActivityEvent__long_
  implements Vector_EventListener_ActivityEvent__long_, Changing, ManagedResource {
    protected DataSource_List_ vector_source;
    protected EventDispatcher_ChangeEvent_ dispatcher = null;

    public EventDispatcher_ChangeEvent_ 
    getChangeEventDispatcher(CallContext context) {
        if( this.dispatcher == null ){
          this.dispatcher = new EventDispatcher_ChangeEvent_(context);
        }
        return this.dispatcher;
    } // getChangeEventDispatcher
    
    public Date 
    getLastUpdate(CallContext call_context) {
        return new Date();
    } // getLastUpdate

    protected VectorImplList_DataSource_EventListener_ActivityEvent__long_ (CallContext context, DataSource_List_ vector_source) {
        this.vector_source = vector_source;
        if( this.vector_source instanceof Changing)  {
          Changing source_changing = (Changing)this.vector_source;
          source_changing.getChangeEventDispatcher(context).addListener(context,this.getChangeEventDispatcher(context));
        } 
    }

    static public VectorImplList_DataSource_EventListener_ActivityEvent__long_ create (CallContext context, DataSource_List_ vector_source) {
        return new VectorImplList_DataSource_EventListener_ActivityEvent__long_(context, vector_source);
    }

    public EventListener_ActivityEvent_ get          (CallContext context, long index) throws DoesNotExist {
        try {
            return (EventListener_ActivityEvent_) vector_source.get(context).get((int) index);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
    }

    public EventListener_ActivityEvent_ tryGet       (CallContext context, long index) {
        try {
            return (EventListener_ActivityEvent_) vector_source.get(context).get((int) index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean  canGet       (CallContext context, long index) {
        return (index >= 0 && index < vector_source.get(context).size()) ? true : false;
    }

    public VectorReferenceToMember_EventListener_ActivityEvent__long_ getReference    (CallContext context, long index) throws DoesNotExist {
        if ( ! canGet(context, index)) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
        return new VectorReferenceToMember_EventListener_ActivityEvent__long_(context, this, index);
    }

    public VectorReferenceToMember_EventListener_ActivityEvent__long_ tryGetReference (CallContext context, long index) {
        if ( ! canGet(context, index)) { return null; }
        return new VectorReferenceToMember_EventListener_ActivityEvent__long_(context, this, index);
    }

    public EventListener_ActivityEvent_ set          (CallContext context, long index, EventListener_ActivityEvent_ item) {
        while (index > vector_source.get(context).size()) { vector_source.get(context).add(null); }
        if( index == vector_source.get(context).size()) {
            vector_source.get(context).add(item);
            return null;
        } else {
            return (EventListener_ActivityEvent_) vector_source.get(context).set((int) index, item);
        }
    }

    public void     add          (CallContext context, long index, EventListener_ActivityEvent_ item) throws AlreadyExists {
        if (index < vector_source.get(context).size()) { AlreadyExists.createAndThrow (context); }
        set(context, index, item);
    }

    public void     prepend      (CallContext context, EventListener_ActivityEvent_ item) {
        if (vector_source.get(context).size() == 0) {
            vector_source.get(context).add(item);
        } else {
            vector_source.get(context).add(0, item);
        }
    }

    public void     append       (CallContext context, EventListener_ActivityEvent_ item) {
        vector_source.get(context).add(item);
    }

    public void     insertBefore (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist {
        try {
            vector_source.get(context).add((int) index, item);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
        }
    }

    public void     insertBehind (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist {
        if (index == vector_source.get(context).size() - 1) {
            vector_source.get(context).add(item);
        } else {
            try {
                vector_source.get(context).add((int) index + 1, item);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow (context);
            }
        }
    }

    public EventListener_ActivityEvent_ replace      (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist {
        try {
            return (EventListener_ActivityEvent_) vector_source.get(context).set((int) index, item);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
            throw (DoesNotExist) null;
        }
    }

    public EventListener_ActivityEvent_ unset        (CallContext context, long index) {
        try {
            return (EventListener_ActivityEvent_) vector_source.get(context).remove((int) index);
        } catch (IndexOutOfBoundsException e) {
            // we kindly ignore this exception
            return null;
        }
    }

    public EventListener_ActivityEvent_ remove       (CallContext context, long index) throws DoesNotExist {
        try {
            return (EventListener_ActivityEvent_) vector_source.get(context).remove((int) index);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null;
        }
    }

    public IteratorItemIndex_EventListener_ActivityEvent__long_ getNavigator (CallContext context) {
        return new VectorIteratorImpl_EventListener_ActivityEvent__long_ (context, this);
    }

    public long     getSize      (CallContext context) {
        return vector_source.get(context).size();
    }

    // to be used with care
    public java.util.List getImplementationList (CallContext context) {
        return this.vector_source.get(context);
    }

    public java.util.Iterator<EventListener_ActivityEvent_> getIterator_EventListener_ActivityEvent__ (CallContext context) {
        return vector_source.get(context).iterator();
    }

    public java.util.Iterator getIterator (CallContext context) {
        return getIterator_EventListener_ActivityEvent__(context);
    }

    public VectorIterable_EventListener_ActivityEvent__long_ getIterable_EventListener_ActivityEvent__ (CallContext context) {
        return new VectorIterable_EventListener_ActivityEvent__long_(context, this);
    }

    public Iterable<EventListener_ActivityEvent_> getIterable (CallContext context) {
        return getIterable_EventListener_ActivityEvent__ (context);
    }
   
    public void release(CallContext context) {
        if (this.vector_source != null && this.vector_source instanceof ManagedResource) {
            ((ManagedResource)(this.vector_source)).release(context);
        }
    }
}
