// instantiated with jti.pl from WriteSet

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

public interface WriteSet_WeakReference_EventListener_ActivityEvent___
{
    // adds item, may already exist
    public void     set     (CallContext context, WeakReference_EventListener_ActivityEvent__ item);

    // adds item, must not already exist
    public void     add     (CallContext context, WeakReference_EventListener_ActivityEvent__ item) throws AlreadyExists;

    // replace item, must already exist
    public void     replace (CallContext context, WeakReference_EventListener_ActivityEvent__ item) throws DoesNotExist;
    // does this method make any sense?
    // should'nt it be? : 
    // public void     replace (CallContext context, WeakReference_EventListener_ActivityEvent__ item, WeakReference_EventListener_ActivityEvent__ item) throws DoesNotExist;

    // removes item, need not exist
    public void     unset   (CallContext context, WeakReference_EventListener_ActivityEvent__ item);

    // removes item, must exist
    public void     remove  (CallContext context, WeakReference_EventListener_ActivityEvent__ item) throws DoesNotExist;
}

