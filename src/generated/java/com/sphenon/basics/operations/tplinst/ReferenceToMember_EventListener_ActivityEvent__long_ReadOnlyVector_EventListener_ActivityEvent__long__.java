// instantiated with jti.pl from ReferenceToMember

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
import com.sphenon.basics.reference.*;
import com.sphenon.basics.many.*;

public interface ReferenceToMember_EventListener_ActivityEvent__long_ReadOnlyVector_EventListener_ActivityEvent__long__
  extends Reference_EventListener_ActivityEvent__
    , ReferenceToMember<EventListener_ActivityEvent_,ReadOnlyVector<EventListener_ActivityEvent_>>
{
    public ReadOnlyVector_EventListener_ActivityEvent__long_ getContainer(CallContext context);
    public long     getIndex    (CallContext context);
}
