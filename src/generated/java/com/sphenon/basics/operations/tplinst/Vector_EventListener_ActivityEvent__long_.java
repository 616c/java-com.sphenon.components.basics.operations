// instantiated with jti.pl from Vector

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

import com.sphenon.basics.many.*;
import com.sphenon.basics.many.returncodes.*;

import com.sphenon.ui.annotations.*;

@UIId("")
@UIName("")
@UIClassifier("Vector_EventListener_ActivityEvent__")
@UIParts("js:instance.getIterable(context)")
public interface Vector_EventListener_ActivityEvent__long_
  extends ReadOnlyVector_EventListener_ActivityEvent__long_,
          WriteVector_EventListener_ActivityEvent__long_
          , GenericVector<EventListener_ActivityEvent_>
          , GenericIterable<EventListener_ActivityEvent_>
{
    public EventListener_ActivityEvent_                                    get             (CallContext context, long index) throws DoesNotExist;
    public EventListener_ActivityEvent_                                    tryGet          (CallContext context, long index);
    public boolean                                     canGet          (CallContext context, long index);

    public ReferenceToMember_EventListener_ActivityEvent__long_ReadOnlyVector_EventListener_ActivityEvent__long__  getReference    (CallContext context, long index) throws DoesNotExist;
    public ReferenceToMember_EventListener_ActivityEvent__long_ReadOnlyVector_EventListener_ActivityEvent__long__  tryGetReference (CallContext context, long index);

    public EventListener_ActivityEvent_                                    set             (CallContext context, long index, EventListener_ActivityEvent_ item);
    public void                                        add             (CallContext context, long index, EventListener_ActivityEvent_ item) throws AlreadyExists;
    public void                                        prepend         (CallContext context, EventListener_ActivityEvent_ item);
    public void                                        append          (CallContext context, EventListener_ActivityEvent_ item);
    public void                                        insertBefore    (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist;
    public void                                        insertBehind    (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist;
    public EventListener_ActivityEvent_                                    replace         (CallContext context, long index, EventListener_ActivityEvent_ item) throws DoesNotExist;
    public EventListener_ActivityEvent_                                    unset           (CallContext context, long index);
    public EventListener_ActivityEvent_                                    remove          (CallContext context, long index) throws DoesNotExist;

    public IteratorItemIndex_EventListener_ActivityEvent__long_       getNavigator    (CallContext context);

    public long                                        getSize         (CallContext context);

    // for sake of Iterable's
    public java.util.Iterator<EventListener_ActivityEvent_>              getIterator_EventListener_ActivityEvent__ (CallContext context);
    public java.util.Iterator                          getIterator (CallContext context);
    public VectorIterable_EventListener_ActivityEvent__long_          getIterable_EventListener_ActivityEvent__ (CallContext context);
    public Iterable<EventListener_ActivityEvent_> getIterable (CallContext context);
}
