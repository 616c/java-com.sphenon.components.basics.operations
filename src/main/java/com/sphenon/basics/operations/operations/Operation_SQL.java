package com.sphenon.basics.operations.operations;

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
import com.sphenon.basics.context.classes.*;
import com.sphenon.basics.configuration.*;
import com.sphenon.basics.message.*;
import com.sphenon.basics.exception.*;
import com.sphenon.basics.notification.*;
import com.sphenon.basics.customary.*;
import com.sphenon.basics.expression.*;
import com.sphenon.basics.expression.returncodes.*;
import com.sphenon.basics.monitoring.*;
import com.sphenon.basics.operations.*;
import com.sphenon.basics.operations.classes.*;
import com.sphenon.basics.data.*;

import java.sql.*;

public class Operation_SQL implements Operation {
    static protected long notification_level;
    static public    long adjustNotificationLevel(long new_level) { long old_level = notification_level; notification_level = new_level; return old_level; }
    static public    long getNotificationLevel() { return notification_level; }
    static { notification_level = NotificationLocationContext.getLevel(RootContext.getInitialisationContext(), "com.sphenon.basics.operations.operations.Operation_SQL"); };

    public Operation_SQL (CallContext context) {
    }

    protected String[] statements;

    public String[] getStatements (CallContext context) {
        return this.statements;
    }

    public void setStatements (CallContext context, String[] statements) {
        this.statements = statements;
    }

    protected String driver;

    public String getDriver (CallContext context) {
        return this.driver;
    }

    public void setDriver (CallContext context, String driver) {
        this.driver = driver;
    }

    protected String connection;

    public String getConnection (CallContext context) {
        return this.connection;
    }

    public void setConnection (CallContext context, String connection) {
        this.connection = connection;
    }

    protected String username;

    public String getUsername (CallContext context) {
        return this.username;
    }

    public void setUsername (CallContext context, String username) {
        this.username = username;
    }

    protected String password;

    public String getPassword (CallContext context) {
        return this.password;
    }

    public void setPassword (CallContext context, String password) {
        this.password = password;
    }

    protected boolean is_query;

    public boolean getIsQuery (CallContext context) {
        return this.is_query;
    }

    public boolean defaultIsQuery (CallContext context) {
        return true;
    }

    public void setIsQuery (CallContext context, boolean is_query) {
        this.is_query = is_query;
    }

    protected String log_level;

    public String getLogLevel (CallContext context) {
        return this.log_level;
    }

    public String defaultLogLevel (CallContext context) {
        return "DIAGNOSTICS";
    }

    public void setLogLevel (CallContext context, String log_level) {
        this.log_level = log_level;
    }

    public Execution execute (CallContext context) {
        return execute(context, null);
    }

    public Execution execute (CallContext context, DataSink<Execution> execution_sink) {
        Execution execution = null;

        try {

            Class.forName(driver).newInstance();
            Connection jdbc_connection = DriverManager.getConnection(connection, username, password);

            Statement jdbc_statement = jdbc_connection.createStatement();

            if (this.statements != null) {
                if (getIsQuery(context)) {
                    for (String statement : this.statements) {
                        ResultSet result_set = jdbc_statement.executeQuery(statement);
                        int columns = result_set.getMetaData().getColumnCount();
                        int row = 0;
                        StringBuilder sb = new StringBuilder();
                        while (result_set.next()) {
                            sb.append(row + ":");
                            for (int i=0; i<columns; i++) {
                                String string = result_set.getString(i+1); // column count starts with 1
                                sb.append((i > 0 ? "," : "") + string);
                            }
                            sb.append("\n");
                        }
                        
                        long ll = NotificationLocationContext.parseProperty(context, log_level, "Log Level in Operation_SQL");
                        if ((notification_level & ll) != 0) { NotificationContext.sendTrace(context, ll, "SQL statement '%(sql)', result '@(result)'", "sql", statement, "result", sb.toString()); }
                    }
                } else {
                    for (String statement : this.statements) {
                        jdbc_statement.executeUpdate(statement);
                    }
                }
            }

            jdbc_connection.close();

            execution = Class_Execution.createExecutionSuccess(context);

        } catch (Throwable t) {
            execution = Class_Execution.createExecutionFailure(context, t);
        }

        if (execution_sink != null) { execution_sink.set(context, execution); }
        return execution;
    }
}
