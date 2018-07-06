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
import com.sphenon.basics.debug.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.processing.*;
import com.sphenon.basics.processing.classes.*;
import com.sphenon.basics.event.*;
import com.sphenon.basics.event.classes.*;
import com.sphenon.formats.json.*;

import com.sphenon.ui.annotations.*;
import com.sphenon.engines.aggregator.annotations.*;

import com.sphenon.basics.operations.*;

import java.io.PrintStream;

public class Class_Execution extends Class_Changing implements Execution, Dumpable, JSONSerialisable {

    public Class_Execution (CallContext context) {
    }

    public Class_Execution (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        this.instruction    = instruction;
        this.problem_state  = problem_state;
        this.problem        = problem;
        this.activity_state = activity_state;
        this.progression    = progression;
        this.record         = record;
    }

    public Class_Execution (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression) {
        this.instruction    = instruction;
        this.problem_state  = problem_state;
        this.problem        = problem;
        this.activity_state = activity_state;
        this.progression    = progression;
        this.record         = null;
    }

    public Class_Execution (CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state) {
        this.instruction    = instruction;
        this.problem_state  = problem_state;
        this.problem        = problem;
        this.activity_state = activity_state;
        this.progression    = null;
        this.record         = null;
    }

    static public Class_Execution createExecutionSuccess(CallContext context) {
        return new Class_Execution (context, null, ProblemState.OK, (Problem) null, ActivityState.COMPLETED, Class_Progression.COMPLETED);
    }

    static public Class_Execution createExecutionSuccess(CallContext context, String instruction_description) {
        return new Class_Execution (context, new Class_Instruction(context, instruction_description), ProblemState.OK, (Problem) null, ActivityState.COMPLETED, Class_Progression.COMPLETED);
    }

    static public Class_Execution createExecutionFailure(CallContext context, Throwable exception) {
        return new Class_Execution (context, null, ProblemState.ERROR, new ProblemException(context, exception), ActivityState.ABORTED);
    }

    static public Class_Execution createExecutionFailure(CallContext context, String problem_message) {
        return new Class_Execution (context, null, ProblemState.ERROR, new ProblemMessage(context, problem_message), ActivityState.ABORTED);
    }

    static public Class_Execution createExecutionFailure(CallContext context, ProblemState problem_state, Problem problem) {
        return new Class_Execution (context, null, problem_state, problem, ActivityState.ABORTED);
    }

    static public Class_Execution createExecutionFailure(CallContext context, String instruction_description, Throwable exception) {
        return new Class_Execution (context, new Class_Instruction(context, instruction_description), ProblemState.ERROR, new ProblemException(context, exception), ActivityState.ABORTED);
    }

    static public Class_Execution createExecutionSkipped(CallContext context) {
        return new Class_Execution (context, null, ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.SKIPPED, Class_Progression.SKIPPED);
    }

    static public Class_Execution createExecutionSkipped(CallContext context, String instruction_description) {
        return new Class_Execution (context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.SKIPPED, Class_Progression.SKIPPED);
    }

    static public Class_Execution createExecutionInProgress(CallContext context) {
        return new Class_Execution (context, null, ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.INPROGRESS, Class_Progression.NO_PROGRESS);
    }

    static public Class_Execution createExecutionInProgress(CallContext context, String instruction_description) {
        return new Class_Execution (context, new Class_Instruction(context, instruction_description), ProblemState.IDLE_INCOMPLETE, (Problem) null, ActivityState.INPROGRESS, Class_Progression.NO_PROGRESS);
    }

    static public Class_Execution createExecution(CallContext context, Instruction instruction, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return new Class_Execution(context, instruction, problem_state, problem, activity_state, progression, record);
    }

    static public Class_Execution createExecution(CallContext context, String instruction_description, ProblemState problem_state, Problem problem, ActivityState activity_state, Progression progression, Record record) {
        return new Class_Execution(context, new Class_Instruction(context, instruction_description), problem_state, problem, activity_state, progression, record);
    }

    @OCPIgnore()
    public void setSuccess(CallContext context) {
        this.disableEvents(context);

        this.setProblemState(context, ProblemState.OK);
        this.setProblem(context, null);
        this.setActivityState(context, ActivityState.COMPLETED);
        this.setProgression(context, Class_Progression.COMPLETED);

        this.enableEvents(context);
        this.notify(context, new ProcessingEvent(context, this));
    }

    @OCPIgnore()
    public void setFailure(CallContext context, Throwable exception) {
        this.disableEvents(context);

        this.setProblemState(context, ProblemState.ERROR);
        this.setProblem(context, new ProblemException(context, exception));
        this.setActivityState(context, ActivityState.ABORTED);

        this.enableEvents(context);
        this.notify(context, new ProcessingEvent(context, this));
    }

    @OCPIgnore()
    public void setFailure(CallContext context, String problem_message) {
        this.disableEvents(context);

        this.setProblemState(context, ProblemState.ERROR);
        this.setProblem(context, new ProblemMessage(context, problem_message));
        this.setActivityState(context, ActivityState.ABORTED);

        this.enableEvents(context);
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public void setInstruction (CallContext context, Instruction instruction) {
        this.instruction = instruction;
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected ProblemState problem_state;

    @UIAttribute(Name="ProblemState",Classifier="ProblemState")
    public ProblemState getProblemState (CallContext context) {
        return this.problem_state;
    }

    public void setProblemState (CallContext context, ProblemState problem_state) {
        this.problem_state = problem_state;
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected Problem problem;

    @UIAttribute(Name="Problem",Value="js:var value = instance.getProblem(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Problem")
    public Problem getProblem (CallContext context) {
        return this.problem;
    }

    public void setProblem (CallContext context, Problem problem) {
        this.problem = problem;
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected ActivityState activity_state;

    @UIAttribute(Name="ActivityState",Classifier="ActivityState")
    public ActivityState getActivityState (CallContext context) {
        return this.activity_state;
    }

    public void setActivityState (CallContext context, ActivityState activity_state) {
        this.activity_state = activity_state;
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected Progression progression;

    @UIAttribute(Name="Progression",Classifier="Progression")
    public Progression getProgression (CallContext context) {
        return this.progression;
    }

    public void setProgression (CallContext context, Progression progression) {
        this.progression = progression;
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected Record record;

    @UIAttribute(Name="Record",Value="js:var value = instance.getRecord(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Record")
    public Record getRecord (CallContext context) {
        return this.record;
    }

    public void setRecord (CallContext context, Record record) {
        this.record = record;
        this.notify(context, new ProcessingEvent(context, this));
    }

    protected Performance performance;

    @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Performance")
    public Performance getPerformance (CallContext context) {
        return this.performance;
    }

    public Execution wait (CallContext context) {
        return this;
    }

    public String toString() {
        return (this.instruction == null ? "" : this.instruction) + ":" + this.problem_state + "/" + this.activity_state + (this.progression != null ? ("/" + this.progression) : "") + (this.record != null ? ("/" + this.record) : "");
    }

    public void dump(CallContext context, DumpNode dump_node) {
        dump_node.dump(context, "Execution     ", this.problem_state + "/" + this.activity_state);
        if (this.instruction != null) {
            dump_node.dump(context, "  Instruction ", this.instruction);
        }
        if (this.progression != null) {
            dump_node.dump(context, "  Progression ", this.progression);
        }
        if (this.getProblemState(context) != null && this.getProblemState(context).isOk(context) == false) {
            if (this.problem != null) {
                dump_node.dump(context, "  Problem     ", this.problem);
            }
            if (this.record != null) {
                dump_node.dump(context, "  Record      ", this.record);
            }
        }
    }

    // -----------------------------------------------------------------------
    // -- OCP Serialise ------------------------------------------------------

    public String ocpDefaultName(CallContext context) {
        return "Execution";
    }

    public String ocpClass(CallContext context) {
        return "Execution";
    }

    public String ocpRetriever(CallContext context) {
        return null;
    }

    public String ocpFactory(CallContext context) {
        return null;
    }

    public boolean ocpContainsData(CallContext context) {
        return false;
    }

    public void ocpSerialise(CallContext context, com.sphenon.engines.aggregator.OCPSerialiser serialiser, boolean as_reference) {
        // if (as_reference) {
        // }
        
        serialiser.serialise(context, this.getProblemState(context), "ProblemState", false);
        serialiser.serialise(context, this.getActivityState(context), "ActivityState", false);
        if (this.getInstruction(context) != null) {
            serialiser.serialise(context, this.getInstruction(context), "Instruction", false);
        }
        if (this.getProgression(context) != null) {
            serialiser.serialise(context, this.getProgression(context), "Progression", false);
        }
        if (this.getProblemState(context) != null && this.getProblemState(context).isOk(context) == false) {
            if (this.getProblem(context) != null) {
                serialiser.serialise(context, this.getProblem(context), "Problem", false);
            }
            if (getRecord(context) != null) {
                serialiser.serialise(context, this.getRecord(context), "Record", false);
            }
            if (getPerformance(context) != null) {
                serialiser.serialise(context, this.getPerformance(context), "Performance", false);
            }
        }
    }

    // -----------------------------------------------------------------------
    // -- JSON Serialise -----------------------------------------------------
    
    public synchronized void jsonSerialise(CallContext context, JSONSerialiser serialiser) throws java.io.IOException {
        serialiser.openObject(context, null);
        jsonSerialiseFields(context, serialiser, true);
        serialiser.closeObject(context);
    }
    
    protected void jsonSerialiseFields(CallContext context, JSONSerialiser serialiser, boolean initial) throws java.io.IOException {
        if (initial) { serialiser.serialise(context, "Execution", "@Class"); }
    
        serialiser.serialise(context, this.getProblemState(context), "ProblemState");
        serialiser.serialise(context, this.getActivityState(context), "ActivityState");
        if (this.getInstruction(context) != null) {
            serialiser.serialise(context, this.getInstruction(context), "Instruction");
        }
        if (this.getProgression(context) != null) {
            serialiser.serialise(context, this.getProgression(context), "Progression");
        }
        if (this.getProblemState(context) != null && this.getProblemState(context).isOk(context) == false) {
            if (this.getProblem(context) != null) {
                serialiser.serialise(context, this.getProblem(context), "Problem");
            }
            if (getRecord(context) != null) {
                serialiser.serialise(context, this.getRecord(context), "Record");
            }
            if (getPerformance(context) != null) {
                serialiser.serialise(context, this.getPerformance(context), "Performance");
            }
        }
    }
}
