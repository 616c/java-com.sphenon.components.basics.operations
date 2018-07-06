// instantiated with jti.pl from SetImpl

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

import com.sphenon.basics.many.returncodes.*;

import java.util.Hashtable;

public class SetImpl_WeakReference_EventListener_ActivityEvent___
  implements Set_WeakReference_EventListener_ActivityEvent___
{
    private java.util.Hashtable map;

    public SetImpl_WeakReference_EventListener_ActivityEvent___ (CallContext context)
    {
    }

    protected void initialise(CallContext context) {
        if (map == null) {
            map = new java.util.Hashtable(4);
        }
    }

    public SetImpl_WeakReference_EventListener_ActivityEvent___ (CallContext context, java.util.Hashtable map)
    {
        this.map = map;
    }

    public boolean  contains (CallContext context, WeakReference_EventListener_ActivityEvent__ item)
    {
        return (map == null ? false : map.containsKey(item));
    }

    public void     set     (CallContext context, WeakReference_EventListener_ActivityEvent__ item)
    {
        initialise(context);
        map.put(item, item);
    }

    public void     add     (CallContext context, WeakReference_EventListener_ActivityEvent__ item) throws AlreadyExists
    {
        initialise(context);
        if (map.containsKey(item)) AlreadyExists.createAndThrow (context);
        map.put(item, item);
    }

    public void     replace (CallContext context, WeakReference_EventListener_ActivityEvent__ item) throws DoesNotExist
    {
        initialise(context);
        if (!map.containsKey(item)) DoesNotExist.createAndThrow (context);
        map.put(item, item);
    }

    public void     unset   (CallContext context, WeakReference_EventListener_ActivityEvent__ item)
    {
        initialise(context);
        map.remove(item);
    }

    public void     remove  (CallContext context, WeakReference_EventListener_ActivityEvent__ item) throws DoesNotExist
    {
        initialise(context);
        if (!map.containsKey(item)) DoesNotExist.createAndThrow (context);
        map.remove(item);
    }

    public Iterator_WeakReference_EventListener_ActivityEvent___ getNavigator (CallContext context)
    {
        initialise(context);
        return new SetIteratorImpl_WeakReference_EventListener_ActivityEvent___ (context, map);
    }

    public long     getSize (CallContext context)
    {
        return (map == null ? 0L : map.size());
    }
}

