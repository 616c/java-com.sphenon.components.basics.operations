package com.sphenon.basics.operations.classes;

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
import com.sphenon.basics.debug.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;
import com.sphenon.basics.event.*;
import com.sphenon.basics.event.classes.*;
import com.sphenon.basics.event.tplinst.*;
import com.sphenon.basics.data.DataSink;
import com.sphenon.basics.data.DataSource;

import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;

import java.io.PrintStream;
import java.util.List;

public class ExecutionSequenceDS extends ExecutionSequence {

    protected DataSource<List<Execution>> executions_source;

    public ExecutionSequenceDS (CallContext context, Instruction instruction, DataSource<List<Execution>> executions_source) {
        super(context, instruction, false);
        this.executions_source = executions_source;
        this.cacheState(context);
    }

    static public ExecutionSequenceDS createExecutionSequenceDS(CallContext context, Instruction instruction, DataSource<List<Execution>> executions_source) {
        return new ExecutionSequenceDS (context, instruction, executions_source);
    }

    static public ExecutionSequenceDS createExecutionSequenceDS(CallContext context, String instruction_description, DataSource<List<Execution>> executions_source) {
        return new ExecutionSequenceDS (context, new Class_Instruction(context, instruction_description), executions_source);
    }

    public List<Execution> getExecutions(CallContext context) {
        return this.executions_source.get(context);
    }
}
