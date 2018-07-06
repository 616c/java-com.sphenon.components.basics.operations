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

public class Operation_FileTreeCopy implements Operation {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.operations.common.Operation_FileTreeCopy"); };

    public Operation_FileTreeCopy (CallContext context) {
    }

    protected String destination_folder;

    public String getDestinationFolder (CallContext context) {
        return this.destination_folder;
    }

    public void setDestinationFolder (CallContext context, String destination_folder) {
        this.destination_folder = destination_folder;
    }

    protected String source_folder;

    public String getSourceFolder (CallContext context) {
        return this.source_folder;
    }

    public void setSourceFolder (CallContext context, String source_folder) {
        this.source_folder = source_folder;
    }

    protected Vector<String> relative_source_pathes;

    public Vector<String> getRelativeSourcePathes (CallContext context) {
        return this.relative_source_pathes;
    }

    public void setRelativeSourcePathes (CallContext context, Vector<String> relative_source_pathes) {
        this.relative_source_pathes = relative_source_pathes;
    }

    public Execution execute (CallContext context) {
        return execute(context, null);
    }

    public Execution execute (CallContext context, DataSink<Execution> execution_sink) {
        Execution execution = null;

        try {
            if ((this.notification_level & Notifier.CHECKPOINT) != 0) { CustomaryContext.create((Context)context).sendTrace(context, Notifier.CHECKPOINT, "Executing file tree copy from '%(source)' to '%(destination)'", "destination", this.destination_folder, "source", this.source_folder); }
            
            File destination_file = new File(this.destination_folder);
            destination_file.mkdirs();
            
            if (destination_file.isDirectory() == false) {
                CustomaryContext.create((Context)context).throwConfigurationError(context, "Target directory '%(destination)' for copy operation does not exist and/or could not be created", "destination", this.destination_folder);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
            
            File source_file = new File(this.source_folder);
            if (source_file.isDirectory() == false) {
                CustomaryContext.create((Context)context).throwConfigurationError(context, "Source directory '%(sourcefolder)' for copy operation does not exist", "sourcefolder", source_folder);
                throw (ExceptionConfigurationError) null; // compiler insists
            }
            
            for (String relative_path : relative_source_pathes) {
                File file_to_copy = new File(source_file, relative_path);
                if (file_to_copy.isFile() == false) {
                    CustomaryContext.create((Context)context).throwConfigurationError(context, "File to copy '%(file)' does not exist or is not a file", "file", file_to_copy);
                    throw (ExceptionConfigurationError) null; // compiler insists
                }
                File copy_target = new File(destination_file, relative_path);
                File target_folder_file = copy_target.getParentFile();
                if (target_folder_file != null) { target_folder_file.mkdirs(); }
                if (target_folder_file == null || target_folder_file.isDirectory() == false) {
                    CustomaryContext.create((Context)context).throwConfigurationError(context, "Target directory '%(targetfolder)' for copy operation does not exist and/or could not be created", "targetfolder", target_folder_file);
                    throw (ExceptionConfigurationError) null; // compiler insists
                }
                FileUtilities.copyFile(context, file_to_copy, copy_target, true);
            }

            execution = Class_Execution.createExecutionSuccess(context, "file tree copy from " + this.source_folder + " to " + this.destination_folder);
        } catch (Throwable t) {
            execution = Class_Execution.createExecutionFailure(context, "file tree copy from " + this.source_folder + " to " + this.destination_folder, t);
        }
        
        if (execution_sink != null) { execution_sink.set(context, execution); }
        return execution;
    }
}
