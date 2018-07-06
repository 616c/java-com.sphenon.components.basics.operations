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
import java.util.Vector;

@UIParts("js:instance.getExecutions(context)")
public class ExecutionTrials extends Class_Changing implements Execution, Dumpable, ContextAware {

    protected Vector<Execution> executions;

    public ExecutionTrials (CallContext context, Instruction instruction, Execution... executions) {
        this.instruction = instruction;
        this.executions = new Vector<Execution>();
        for (Execution execution : executions) {
            this.addExecution(context, execution, false);
        }
        this.cacheState(context);
    }

    static public ExecutionTrials createExecutionTrials(CallContext context, Instruction instruction, Execution... execution) {
        return new ExecutionTrials (context, instruction, execution);
    }

    static public ExecutionTrials createExecutionTrials(CallContext context, String instruction_description, Execution... execution) {
        return new ExecutionTrials (context, new Class_Instruction(context, instruction_description), execution);
    }

    public Vector<Execution> getExecutions(CallContext context) {
        return this.executions;
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
        this.executions.add(execution);
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

    protected synchronized void notifyIfChanged(CallContext context) {
        if (this.hasChanged(context)) {
            this.getChangeEventDispatcher(context).notify(context, new ProcessingEvent(context, this));
            this.cacheState(context);
        }
    }

    protected ProblemState  cached_problem_state;
    protected ActivityState cached_activity_state;
    protected Progression   cached_progression;

    protected void cacheState(CallContext context) {
        this.cached_problem_state = this.getProblemState(context);
        this.cached_activity_state = this.getActivityState(context);
        this.cached_progression = this.getProgression(context);
        if (this.cached_progression != null) {
            this.cached_progression = this.cached_progression.getSnapshot(context);
        }
    }

    protected boolean hasChanged(CallContext context) {
        return (    ( this.cached_problem_state == null ?
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
               ) ? false : true;
    }

    protected Instruction instruction;

    public Instruction getInstruction (CallContext context) {
        return this.instruction;
    }

    public ProblemState getProblemState (CallContext context) {
        ProblemState problem_state = ProblemState.IDLE;
        boolean got_completed = false;
        if (this.executions != null) {
            for (Execution execution : this.executions) {
                if (execution.getActivityState(context) == ActivityState.COMPLETED) {
                    got_completed = true;
                    break;
                }
            }
            for (Execution execution : this.executions) {
                // if there's no completed one, we combine all
                // otherwise, we combine only all completed ones
                if (    got_completed == false
                     || (    got_completed == true
                          && execution.getActivityState(context) == ActivityState.COMPLETED
                        )
                   ) {
                    problem_state = problem_state.combineWith(context, execution.getProblemState(context));
                }
            }
        }
        return problem_state;
    }

    public Problem getProblem (CallContext context) {
        Problem[] problems = new Problem[executions.size()];
        boolean got_one = false;
        int i=0;
        for (Execution execution : this.executions) {
            if ((problems[i++] = execution.getProblem(context)) != null) {
                got_one = true;
            }
        }
        return got_one ? new ProblemGroup(context, problems) : null;
    }

    public ActivityState getActivityState (CallContext context) {
        ActivityState activity_state = ActivityState.UNREADY;
        if (this.executions != null) {
            for (Execution execution : this.executions) {
                activity_state = activity_state.combineWith(context, execution.getActivityState(context), true);
            }
        }
        return activity_state;
    }

    public Progression getProgression (CallContext context) {
        Progression[] progressions = new Progression[executions == null ? 0 : executions.size()];
        if (this.executions != null) {
            int i=0;
            for (Execution execution : this.executions) {
                progressions[i++] = execution.getProgression(context);
            }
        }
        return new ProgressionGroup(context, true, progressions);
    }

    public Record getRecord (CallContext context) {
        Record[] records = new Record[executions == null ? 0 : executions.size()];
        boolean got_one = false;
        if (this.executions != null) {
            int i=0;
            for (Execution execution : this.executions) {
                records[i++] = execution.getRecord(context);
                got_one = true;
            }
        }
        return got_one ? new RecordGroup(context, records) : null;
    }

    @UIAttribute(Name="Performance",Value="js:var value = instance.getPerformance(context); Packages.com.sphenon.basics.debug.Dumper.dumpToString(context, null, value == null ? '' : value)")
    public Performance getPerformance (CallContext context) {
        if (this.executions == null || this.executions.size() == 0) {
            return null;
        }
        Performance performance = null; // ...
        for (Execution execution : this.executions) {
            // performance = performance.add(context, execution.getPerformance(context));
        }
        return performance;
    }

    public Execution wait (CallContext context) {
        for (Execution execution : this.executions) {
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
            for (Execution execution : this.executions) {
                dn.dump(context, (new Integer(i++)).toString(), execution);
            }
            dn.close(context);
        }
    }
}
