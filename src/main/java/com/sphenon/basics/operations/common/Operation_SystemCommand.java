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
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.data.*;

import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.classes.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;

import com.sphenon.ui.annotations.*;

import java.io.InputStreamReader;

@UIId         ("operation")
@UIName       ("System Command")
public class Operation_SystemCommand implements Operation {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.operations.common.Operation_SystemCommand"); };

    public Operation_SystemCommand (CallContext context) {
    }

    protected String system_command;

    @UIAttribute(Name="Command")
    public String getSystemCommand (CallContext context) {
        return this.system_command;
    }

    public void setSystemCommand (CallContext context, String system_command) {
        this.system_command = system_command;
    }

    protected String working_folder;

    @UIAttribute(Name="Working Folder")
    public String getWorkingFolder (CallContext context) {
        return this.working_folder;
    }

    public void setWorkingFolder (CallContext context, String working_folder) {
        this.working_folder = working_folder;
    }

    public Execution execute (CallContext context) {
        return execute(context, null);
    }

    public Execution execute (CallContext context, DataSink<Execution> execution_sink) {
        Execution execution = null;

        // [ToDo: asynchron!] implementation in VUIEntityProcess_SystemProcess.java already available

        String            out = "";
        String            err = "";

        InputStreamReader process_stdout = null;
        InputStreamReader process_stderr = null;
        int               exit_value = 0;

        if ((this.notification_level & Notifier.CHECKPOINT) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.CHECKPOINT, "Executing system command: '%(command)' in folder '%(folder)'", "command", this.system_command, "folder", this.working_folder); }

        try {
            String[] cmda = { "bash", "-c", this.system_command };

            java.lang.Process process = java.lang.Runtime.getRuntime().exec(cmda, null, new java.io.File(this.working_folder));
            // process.waitFor()

            if (process != null) {
                process_stdout = new InputStreamReader(process.getInputStream());
                process_stderr = new InputStreamReader(process.getErrorStream());
                char [] buf = new char[1];
                do {
                    Thread.currentThread().sleep(500);
                    while (process_stdout.ready()) {
                        process_stdout.read(buf, 0, 1);
                        out += buf[0];
                    }
                    while (process_stderr.ready()) {
                        process_stderr.read(buf, 0, 1);
                        err += buf[0];
                    }
                    try {
                        exit_value = process.exitValue();
                        break;
                    } catch (IllegalThreadStateException itse) {
                        continue;
                    }
                } while (true);
            }

            if (exit_value != 0) {
                Record record = new Class_RecordStandardLog(context, out, err);
                if ((this.notification_level & Notifier.CHECKPOINT) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.CHECKPOINT, "Executing failed, exit value: '%(exit)', record: '%(record)", "exit", exit_value, "record", record); }

                execution = Class_Execution.createExecution(context, this.system_command, ProblemState.ERROR, new ProblemReturnCode(context, exit_value), ActivityState.ABORTED, null, record);
            } else {

                execution = Class_Execution.createExecution(context, this.system_command, ProblemState.OK, (Problem) null, ActivityState.COMPLETED, null, new Class_RecordStandardLog(context, out, err));
            }

        } catch (Throwable t) {
            execution = Class_Execution.createExecution(context, this.system_command, ProblemState.ERROR, new ProblemException(context, t), ActivityState.ABORTED, null, new Class_RecordStandardLog(context, out, err));
        }
        
        if (execution_sink != null) { execution_sink.set(context, execution); }
        return execution;
    }
}
