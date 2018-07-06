package com.sphenon.basics.operations.common;

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

import com.sphenon.basics.context.*;
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.system.*;
import com.sphenon.basics.data.*;

import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.classes.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;

import java.io.InputStreamReader;
import java.util.Vector;
import java.io.File;

public class Operation_SaveToFile implements Operation {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.operations.common.Operation_SaveToFile"); };

    public Operation_SaveToFile (CallContext context) {
    }

    protected String destination_file_name;

    public String getDestinationFileName (CallContext context) {
        return this.destination_file_name;
    }

    public void setDestinationFileName (CallContext context, String destination_file_name) {
        this.destination_file_name = destination_file_name;
    }

    protected String string;

    public String getString (CallContext context) {
        return this.string;
    }

    public String defaultString (CallContext context) {
        return null;
    }

    public void setString (CallContext context, String string) {
        this.string = string;
    }

    protected DataSource<String> string_source;

    public DataSource<String> getStringSource (CallContext context) {
        return this.string_source;
    }

    public DataSource<String> defaultStringSource (CallContext context) {
        return null;
    }

    public void setStringSource (CallContext context, DataSource<String> string_source) {
        this.string_source = string_source;
    }

    public Execution execute (CallContext context) {
        return execute(context, null);
    }

    public Execution execute (CallContext context, DataSink<Execution> execution_sink) {
        Execution execution = null;

        try {
            if ((this.notification_level & Notifier.CHECKPOINT) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.CHECKPOINT, "Executing save data to file '%(destination)'", "destination", this.destination_file_name); }
            
            File destination_file = new File(this.destination_file_name);
            SystemCommandUtilities.ensureParentFolderExists(context, destination_file);
            
            if (this.getString(context) != null) {
                FileUtilities.writeFile(context, destination_file, this.getString(context));
            }
            if (this.getStringSource(context) != null) {
                FileUtilities.writeFile(context, destination_file, this.getStringSource(context).get(context));
            }

            execution = Class_Execution.createExecution(context, "save data to file " + this.destination_file_name, ProblemState.OK, (Problem) null, ActivityState.COMPLETED, null, null);
        } catch (Throwable t) {
            execution = Class_Execution.createExecution(context, "save data to file " + this.destination_file_name, ProblemState.ERROR, new ProblemException(context, t), ActivityState.ABORTED, null, null);
        }
        
        if (execution_sink != null) { execution_sink.set(context, execution); }
        return execution;
    }
}
