package com.sphenon.basics.operations;

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
import com.sphenon.basics.message.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.classes.*;
import com.sphenon.basics.expression.returncodes.*;

public class ExpressionEvaluator_Operation implements ExpressionEvaluator {

    public ExpressionEvaluator_Operation (CallContext context) {
        this.result_attribute = new Class_ActivityAttribute(context, "Result", "Object", "-", "*");
        this.activity_interface = new Class_ActivityInterface(context);
        this.activity_interface.addAttribute(context, this.result_attribute);
    }

    protected Class_ActivityInterface activity_interface;
    protected ActivityAttribute result_attribute;

    public String[] getIds(CallContext context) {
        return new String[] { "op", "operation" };
    }

    public Object evaluate(CallContext context, String string, Scope scope) throws EvaluationFailure {
        try {
            ExpressionEvaluator evaluator = ExpressionEvaluatorRegistry.getDefaultExpressionRegistry(context).retrieve(context, "tocp", false);

            Object object = evaluator.evaluate(context, string, scope);

            if ((object instanceof Operation) == false) {
                EvaluationFailure.createAndThrow(context, "Operation expression '%(expression)' does not denote an 'Operation', but a '%(class)'", "expression", null, "class", object == null ? "null" : object.getClass().getName().replaceFirst(".*\\.", ""));
                throw (EvaluationFailure) null;
            }

            Operation operation = (Operation) object;

            return operation.execute(context);
        } catch (Throwable t) {
            EvaluationFailure.createAndThrow(context, t, "Evaluation failure");
            throw (EvaluationFailure) null;
        }
    }

    public ActivityClass parse(CallContext context, ExpressionSource expression_source) throws EvaluationFailure {
        return new ActivityClass_ExpressionEvaluator(context, this, expression_source, this.activity_interface, this.result_attribute);
    }
}
