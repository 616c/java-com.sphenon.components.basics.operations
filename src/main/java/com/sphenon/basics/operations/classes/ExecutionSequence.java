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

import com.sphenon.ui.annotations.*;

import com.sphenon.basics.operations.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

@UIParts("js:instance.getExecutions(context)")
public class ExecutionSequence extends Class_Changing implements Execution, Dumpable, ContextAware {

    protected ExecutionSequence (CallContext context, Instruction instruction, boolean initialise, Execution... executions) {
        this.instruction = instruction;
        this.expected_executions = -1;
        this.weights = null;
        if (executions != null) {
            for (Execution execution : executions) {
                this.addExecution(context, execution, false);
            }
        }
        if (initialise) {
            this.cacheState(context);
        }
    }

    public ExecutionSequence (CallContext context, Instruction instruction, Execution... executions) {
        this(context, instruction, true, executions);
    }

    protected List<Execution> executions;

    public List<Execution> getExecutions(CallContext context) {
        if (this.executions == null) {
            this.executions = new Vector<Execution>();
        }
        return this.executions;
    }

    static public ExecutionSequence createExecutionSequence(CallContext context, Instruction instruction, Execution... execution) {
        return new ExecutionSequence (context, instruction, execution);
    }

    static public ExecutionSequence createExecutionSequence(CallContext context, String instruction_description, Execution... execution) {
        return new ExecutionSequence (context, new Class_Instruction(context, instruction_description), execution);
    }

    protected DataSink<Execution> execution_add_sink;

    public DataSink<Execution> getExecutionAddSink(CallContext context) {
        if (execution_add_sink == null) {
            execution_add_sink = new DataSink<Execution>() {
                public void setObject(CallContext context, Object data) {
                    set(context, (Execution) data);
                }
                public void set(CallContext context, Execution execution) {
                    addExecution(context, execution);
                }
            };
        }
        return execution_add_sink;
    }

    public void addExecution(CallContext context, Execution execution) {
        this.addExecution(context, execution, true);
    }

    protected EventListener_ChangeEvent_ child_listener;

    public void addExecution(CallContext context, Execution execution, boolean notify) {
        this.getExecutions(context).add(execution);
        if (execution instanceof Changing) {
            if (this.child_listener == null) {
                this.child_listener = new EventListener_ChangeEvent_() {
                        public void notify(CallContext context) {
                            notifyIfChanged(context);
                        }
                        public void notify(CallContext context, ChangeEvent event) {
                            notify(context);
                        }
                    };
            }
            ((Changing) execution).getChangeEventDispatcher(context).addListener(context, this.child_listener);
        }
        if (notify) {
            this.notifyIfChanged(context);
        }
    }

    protected int expected_executions;

    public int getExpectedExecutions (CallContext context) {
        return this.expected_executions;
    }

    public void setExpectedExecutions (CallContext context, int expected_executions) {
        this.expected_executions = expected_executions;
    }

    protected float[] weights;

    public float[] getWeights (CallContext context) {
        return this.weights;
    }

    public void setWeights (CallContext context, float[] weights) {
        this.weights = weights;
    }

    public synchronized void notifyIfChanged(CallContext context) {
        if (this.hasChanged(context)) {
            this.getChangeEventDispatcher(context).notify(context, new ProcessingEvent(context, this));
            this.cacheState(context);
        }
    }

    protected ProblemState  cached_problem_state;
    protected Problem       cached_problem;
    protected ActivityState cached_activity_state;
    protected Progression   cached_progression;
    protected Record        cached_record;

    protected void cacheState(CallContext context) {
        this.cached_problem_state = this.getProblemState(context);
        this.cached_activity_state = this.getActivityState(context);
        this.cached_progression = this.getProgression(context);
        if (this.cached_progression != null) {
            this.cached_progression = this.cached_progression.getSnapshot(context);
        }
    }

    protected boolean hasChanged(CallContext context) {
        return (    closed
                 || (    ( this.cached_problem_state == null ?
                             this.getProblemState(context) == null
                           : this.cached_problem_state.equals(this.getProblemState(context))
                         )
                      && ( this.cached_activity_state == null ?
                             this.getActivityState(context) == null
                           : this.cached_activity_state.equals(this.getActivityState(context))
                         )
                      && ( this.cached_progression == null ?
                             this.getProgression(context) == null
                           : this.cached_progression.equals(this.getProgression(context))
                         )
                    )
               ) ? false : true;
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public ProblemState getProblemState (CallContext context) {
        if (this.closed && this.cached_problem_state != null) {
            return this.cached_problem_state;
        }
        ProblemState problem_state = ProblemState.IDLE;
        for (Execution execution : this.getExecutions(context)) {
            problem_state = problem_state.combineWith(context, execution.getProblemState(context));
        }
        if (this.closed) {
            this.cached_problem_state = problem_state;
        }
        return problem_state;
    }

    public Problem getProblem (CallContext context) {
        if (this.closed && this.cached_problem != null) {
            return this.cached_problem;
        }
        Problem[] problems = new Problem[this.getExecutions(context).size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.getExecutions(context)) {
            if ((problems[i++] = execution.getProblem(context)) != null) {
                got_one = true;
            }
        }
        Problem problem = got_one ? new ProblemGroup(context, problems) : null;
        if (this.closed) {
            this.cached_problem = problem;
            if (this.cached_problem == null) {
                this.cached_problem = ProblemEmpty.get(context);
            }
        }
        return problem;
    }

    protected boolean closed;

    public void close(CallContext context) {
        this.closed = true;

        // so that it is recalculated one more time
        this.cached_problem_state  = null;
        this.cached_activity_state = null;
        this.cached_progression    = null;
    }

    public ActivityState getActivityState (CallContext context) {
        if (this.closed && this.cached_activity_state != null) {
            return this.cached_activity_state;
        }
        ActivityState activity_state;
        if (this.getExecutions(context).size() == 0) {
            activity_state = this.closed || this.expected_executions == 0 ? ActivityState.COMPLETED : this.expected_executions == -1 ? ActivityState.UNREADY : ActivityState.INPROGRESS;
        } else {
            activity_state = this.expected_executions == -1 || this.getExecutions(context).size() >= this.expected_executions ? ActivityState.COMPLETED : ActivityState.INPROGRESS;
            for (Execution execution : this.getExecutions(context)) {
                activity_state = activity_state.combineWith(context, execution.getActivityState(context));
            }
        }
        if (this.closed) {
            this.cached_activity_state = activity_state;
        }
        return activity_state;
    }

    public Progression getProgression (CallContext context) {
        if (this.closed && this.cached_progression != null) {
            return this.cached_progression;
        }
        int size = this.expected_executions;
        if (this.getExecutions(context).size() > this.expected_executions) { size = this.getExecutions(context).size(); }
        Progression[] progressions = new Progression[size];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.getExecutions(context)) {
            if ((progressions[i] = execution.getProgression(context)) != null) {
                got_one = true;
            } else {
                progressions[i] = Class_Progression.NO_PROGRESS;
            }
            i++;
        }
        if (this.getExecutions(context).size() < this.expected_executions) {
            for (int e=this.getExecutions(context).size(); e<this.expected_executions; e++) {
                progressions[i++] = Class_Progression.NO_PROGRESS;
            }
        }
        Progression progression = new ProgressionGroup(context, this.weights, progressions);
        if (this.closed) {
            progression = progression.getSnapshot(context);
            this.cached_progression = progression;
        }
        return progression;
    }

    public Record getRecord (CallContext context) {
        if (this.closed && this.cached_record != null) {
            return this.cached_record;
        }
        Record[] records = new Record[this.getExecutions(context).size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.getExecutions(context)) {
            if ((records[i++] = execution.getRecord(context)) != null) {
                got_one = true;
            }
        }
        Record record = got_one ? new RecordGroup(context, records) : null;
        if (this.closed) {
            this.cached_record = record;
            if (this.cached_record == null) {
                this.cached_record = RecordEmpty.get(context);
            }
        }
        return record;
    }

    /* workaround: disabled for performance reasons, since UI dumped whole tree
       not dynamically; possibly reenable as well as annotate all other attribute
       like in Class_Execution
       @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)",Classifier="Performance")
    */
    public Performance getPerformance (CallContext context) {
        if (this.getExecutions(context).size() == 0) {
            return null;
        }
        Performance performance = null; // ...
        // for (Execution execution : this.getExecutions(context)) {
        //     performance = performance.add(context, execution.getPerformance(context));
        // }
        return performance;
    }

    public Execution wait (CallContext context) {
        for (Execution execution : this.getExecutions(context)) {
            execution.wait(context);
        }
        return this;
    }

    public String toString(CallContext context) {
        ProblemState  problem_state  = getProblemState(context);
        Problem       problem        = getProblem(context);
        ActivityState activity_state = getActivityState(context);
        Progression   progression    = getProgression(context);
        Record        record         = getRecord(context);
        Instruction   instruction    = getInstruction(context);

        return problem_state + "/" + activity_state + (progression != null ? ("/" + ContextAware.ToString.convert(context, progression)) : "") + (instruction != null ? ("/" + ContextAware.ToString.convert(context, instruction)) : "") + (record != null ? ("/" + ContextAware.ToString.convert(context, record)) : "");
    }

    public String toString() {
        return toString(RootContext.getFallbackCallContext());
    }

    public void dump(CallContext context, DumpNode dump_node) {
        ProblemState  problem_state  = getProblemState(context);
        Problem       problem        = getProblem(context);
        ActivityState activity_state = getActivityState(context);
        Progression   progression    = getProgression(context);
        Record        record         = getRecord(context);

        dump_node.dump(context, "Execution    ", problem_state + "/" + activity_state);
        if (progression != null) {
            dump_node.dump(context, "- Progression", progression);
        }
        if (this.getProblemState(context) != null && this.getProblemState(context).isOk(context) == false) {
            if (problem != null) {
                dump_node.dump(context, "- Problem    ", problem);
            }
            if (record != null) {
                dump_node.dump(context, "- Record     ", record);
            }
            DumpNode dn = dump_node.openDump(context, "- Executions ");
            int i=1;
            for (Execution execution : this.getExecutions(context)) {
                dn.dump(context, (new Integer(i++)).toString(), execution);
            }
            dn.close(context);
        }
    }
}
