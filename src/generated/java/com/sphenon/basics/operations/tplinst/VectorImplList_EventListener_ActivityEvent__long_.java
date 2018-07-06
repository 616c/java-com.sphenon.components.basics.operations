// instantiated with jti.pl from VectorImplList

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
import com.sphenon.basics.debug.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.many.*;

import com.sphenon.basics.many.returncodes.*;

public class VectorImplList_EventListener_ActivityEvent__long_
  implements Vector_EventListener_ActivityEvent__long_, VectorImplList, Dumpable, ManagedResource {
    protected java.util.List vector;

    protected VectorImplList_EventListener_ActivityEvent__long_ (CallContext context) {
        vector = new java.util.ArrayList ();
    }

    static public VectorImplList_EventListener_ActivityEvent__long_ create (CallContext context) {
        return new VectorImplList_EventListener_ActivityEvent__long_(context);
    }

    protected VectorImplList_EventListener_ActivityEvent__long_ (CallContext context, java.util.List vector) {
        this.vector = vector;
    }

    static public VectorImplList_EventListener_ActivityEvent__long_ create (CallContext context, java.util.List vector) {
        return new VectorImplList_EventListener_ActivityEvent__long_(context, vector);
    }

    public EventListener_ActivityEvent_ get          (CallContext context, long index) throws DoesNotExist {
        try {
            return (EventListener_ActivityEvent_) vector.get((int) index);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null; // compiler insists
        }
    }

    public EventListener_ActivityEvent_ tryGet       (CallContext context, long index) {
        if (index < 0 || index >= vector.size()) {
            return null;
        }
        return (EventListener_ActivityEvent_) vector.get((int) index);
    }

    public boolean  canGet       (CallContext context, long index) {
        return (index >= 0 && index < vector.size()) ? true : false;
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
        while (index > vector.size()) { vector.add(null); }
        if( index == vector.size()) {
            vector.add(item);
            return null;
        } else {
            return (EventListener_ActivityEvent_) vector.set((int) index, item);
        }
    }

    public void     add          (CallContext context, long index, EventListener_ActivityEvent_ item) throws AlreadyExists {
        if (index < vector.size()) { AlreadyExists.createAndThrow (context); }
        set(context, index, item);
    }

    public void     prepend      (CallContext call_context, EventListener_ActivityEvent_ item) {
        if (vector.size() == 0) {
            vector.add(item);
        } else {
            vector.add(0, item);
        }
    }

    public void     append       (CallContext context, EventListener_ActivityEvent_ item) {
        vector.add(item);
    }

    public void     insertBefore (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist {
        try {
            vector.add((int) index, item);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(context);
        }
    }

    public void     insertBehind (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist {
        if (index == vector.size() - 1) {
            vector.add(item);
        } else {
            try {
                vector.add((int) index + 1, item);
            } catch (IndexOutOfBoundsException e) {
                DoesNotExist.createAndThrow (context);
            }
        }
    }

    public EventListener_ActivityEvent_ replace      (CallContext call_context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist {
        try {
            return (EventListener_ActivityEvent_) vector.set((int) index, item);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow(call_context);
            throw (DoesNotExist) null;
        }
    }

    public EventListener_ActivityEvent_ unset        (CallContext context, long index) {
        try {
            return (EventListener_ActivityEvent_) vector.remove((int) index);
        } catch (IndexOutOfBoundsException e) {
            // we kindly ignore this exception
            return null;
        }
    }

    public EventListener_ActivityEvent_ remove       (CallContext context, long index) throws DoesNotExist {
        try {
            return (EventListener_ActivityEvent_) vector.remove((int) index);
        } catch (IndexOutOfBoundsException e) {
            DoesNotExist.createAndThrow (context);
            throw (DoesNotExist) null;
        }
    }

    public IteratorItemIndex_EventListener_ActivityEvent__long_ getNavigator (CallContext context) {
        return new VectorIteratorImpl_EventListener_ActivityEvent__long_ (context, this);
    }

    public long     getSize      (CallContext context) {
        return vector.size();
    }

    // to be used with care
    public java.util.List getImplementationList (CallContext context) {
        return this.vector;
    }

    public java.util.Iterator<EventListener_ActivityEvent_> getIterator_EventListener_ActivityEvent__ (CallContext context) {
        return vector.iterator();
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
        if (this.vector != null && this.vector instanceof ManagedResource) {
            ((ManagedResource)(this.vector)).release(context);
        }
    }

    public void dump(CallContext context, DumpNode dump_node) {
        int i=1;
        for (Object o : vector) {
            dump_node.dump(context, (new Integer(i++)).toString(), o);
        }
    }
}
