// instantiated with jti.pl from Factory_Vector

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

public class Factory_Vector_EventListener_ActivityEvent__long_
{
    private String[] names;
    private EventListener_ActivityEvent_[] values;

    static public Vector_EventListener_ActivityEvent__long_ construct (CallContext context) {
        return (new Factory_Vector_EventListener_ActivityEvent__long_(context)).create(context);
    }

    public Factory_Vector_EventListener_ActivityEvent__long_ (CallContext context) {
    }

    public Factory_Vector_EventListener_ActivityEvent__long_ () {
    }

    protected Vector_EventListener_ActivityEvent__long_ instance;

    public Vector_EventListener_ActivityEvent__long_ precreate (CallContext context) {
        return this.instance = VectorImpl_EventListener_ActivityEvent__long_.create(context);
    }

    public Vector_EventListener_ActivityEvent__long_ create (CallContext context) {
        Vector_EventListener_ActivityEvent__long_ vector = (this.instance != null ? this.instance : VectorImpl_EventListener_ActivityEvent__long_.create(context));
        this.instance = null;
        if (names != null && values != null) {
            for (int i=0; i<names.length; i++) {
                vector.set(context, i, values[i]); // naja, eigentlich "add" statt "set"
            }
        }
        return vector;
    }

    public void set_ParametersAtOnce(CallContext call_context, String[] names, EventListener_ActivityEvent_[] values) {
        if (names.length != values.length) {
            Context context = Context.create(call_context);
            CustomaryContext cc = CustomaryContext.create(context);
            cc.throwPreConditionViolation(context, ManyStringPool.get(context, "0.5.0" /* Number of names differs from number of values */));
        }
        this.names = names;
        this.values = values;
    }
}
