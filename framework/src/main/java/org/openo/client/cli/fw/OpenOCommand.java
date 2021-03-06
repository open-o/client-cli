/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.client.cli.fw;

import org.openo.client.cli.fw.ad.OpenOAuthClient;
import org.openo.client.cli.fw.ad.OpenOCredentials;
import org.openo.client.cli.fw.ad.OpenOService;
import org.openo.client.cli.fw.conf.Constants;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandHelpFailed;
import org.openo.client.cli.fw.error.OpenOCommandInvalidParameterType;
import org.openo.client.cli.fw.error.OpenOCommandInvalidPrintDirection;
import org.openo.client.cli.fw.error.OpenOCommandInvalidResultAttributeScope;
import org.openo.client.cli.fw.error.OpenOCommandInvalidSchema;
import org.openo.client.cli.fw.error.OpenOCommandInvalidSchemaVersion;
import org.openo.client.cli.fw.error.OpenOCommandNotInitialized;
import org.openo.client.cli.fw.error.OpenOCommandParameterNameConflict;
import org.openo.client.cli.fw.error.OpenOCommandParameterOptionConflict;
import org.openo.client.cli.fw.error.OpenOCommandRegistrationFailed;
import org.openo.client.cli.fw.error.OpenOCommandSchemaNotFound;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.output.OpenOCommandResult;
import org.openo.client.cli.fw.output.OpenOCommandResultAttributeScope;
import org.openo.client.cli.fw.output.ResultType;
import org.openo.client.cli.fw.utils.OpenOCommandUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OpenO Command.
 *
 */
public abstract class OpenOCommand {

    private String cmdDescription;

    private String cmdName;

    private String cmdSchemaName;

    private OpenOService openOservice = new OpenOService();

    private List<OpenOCommandParameter> cmdParameters = new ArrayList<>();

    private OpenOCommandResult cmdResult = new OpenOCommandResult();

    protected OpenOAuthClient authClient;

    protected boolean isInitialzied = false;

    public String getSchemaVersion() {
        return Constants.OPENO_CMD_SCHEMA_VERSION_VALUE;
    }

    /**
     * OpenO command description, defined by derived command.
     */
    public String getDescription() {
        return this.cmdDescription;
    }

    public void setDescription(String description) {
        this.cmdDescription = description;
    }

    /*
     * OpenO command name like user-create, ns-list, etc , defined by derived command
     */
    public String getName() {
        return this.cmdName;
    }

    public void setName(String name) {
        this.cmdName = name;
    }

    /*
     * OpenO service, this command uses to execute it. , defined by derived command
     */
    public OpenOService getService() {
        return this.openOservice;
    }

    public void setService(OpenOService service) {
        this.openOservice = service;
    }

    public void setParameters(List<OpenOCommandParameter> parameters) {
        this.cmdParameters = parameters;
    }

    /*
     * OpenO command input parameters, defined by derived command
     */
    public List<OpenOCommandParameter> getParameters() {
        return this.cmdParameters;
    }

    /*
     * OpenO command input parameters, defined by derived command
     */
    public Map<String, OpenOCommandParameter> getParametersMap() {
        return OpenOCommandUtils.getInputMap(this.getParameters());
    }

    /*
     * OpenO command output results, defined by derived command
     */
    public OpenOCommandResult getResult() {
        return this.cmdResult;
    }

    public void setResult(OpenOCommandResult result) {
        this.cmdResult = result;
    }

    public String getSchemaName() {
        return cmdSchemaName;
    }

    protected void setSchemaName(String schemaName) {
        this.cmdSchemaName = schemaName;
    }

    /**
     * Initialize this command from command schema.
     *
     * @throws OpenOCommandRegistrationFailed
     *             Command Registration Exception
     * @throws OpenOCommandInvalidResultAttributeScope
     *             InvalidResultAttribute Exception
     * @throws OpenOCommandInvalidPrintDirection
     *             InvalidPrintDirection Exception
     * @throws OpenOCommandInvalidParameterType
     *             InvalidParameterType Exception
     * @throws OpenOCommandSchemaNotFound
     *             SchemaNotFound Exception
     * @throws OpenOCommandInvalidSchema
     *             InvalidSchema Exception
     * @throws OpenOCommandParameterOptionConflict
     *             ParameterOptionConflict Exception
     * @throws OpenOCommandParameterNameConflict
     *             ParameterNameConflict Exception
     * @throws OpenOCommandInvalidSchemaVersion
     *             InvalidSchemaVersion Exception
     */
    public void initializeSchema(String schema) throws OpenOCommandException {
        this.setSchemaName(schema);
        OpenOCommandUtils.loadSchema(this, schema, true);
        this.initializeProfileSchema();
        this.isInitialzied = true;
    }

    /**
     * Any additional profile based such as http/swagger schema could be initialized.
     */
    protected void initializeProfileSchema() throws OpenOCommandException {

    }

    /*
     * Validate input parameters. This can be overridden in derived commands
     */
    protected void validate() throws OpenOCommandException {
        for (OpenOCommandParameter param : this.getParameters()) {
            param.validate();
        }
    }

    /**
     * OpenO command execute with given parameters on service. Before calling this method, its mandatory to set all
     * parameters value.
     *
     * @throws OpenOCommandException
     *             : General Command Exception
     */
    public OpenOCommandResult execute() throws OpenOCommandException {
        if (!this.isInitialzied) {
            throw new OpenOCommandNotInitialized(this.getClass().getName());
        }

        Map<String, OpenOCommandParameter> paramMap = this.getParametersMap();

        // -h or --help is always higher precedence !, user can set this value to get help message
        if ("true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_HELP).getValue())) {
            OpenOCommandResult result = new OpenOCommandResult();
            result.setType(ResultType.TEXT);
            result.setOutput(this.printHelp());
            return result;
        }

        // -v or --version is next higher precedence !, user can set this value to get help message
        if ("true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_VERSION).getValue())) {
            OpenOCommandResult result = new OpenOCommandResult();
            result.setType(ResultType.TEXT);
            result.setOutput(this.printVersion());
            return result;
        }

        // validate
        this.validate();

        // -f or --format
        this.cmdResult.setType(
                ResultType.get(paramMap.get(Constants.DEFAULT_PARAMETER_OUTPUT_FORMAT).getValue().toString()));
        if ("true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_OUTPUT_ATTR_LONG).getValue())) {
            this.cmdResult.setScope(OpenOCommandResultAttributeScope.LONG);
        }
        // --no-title
        if ("true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_OUTPUT_NO_TITLE).getValue())) {
            this.cmdResult.setIncludeTitle(false);
        }

        // --debug
        if ("true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_DEBUG).getValue())) {
            this.cmdResult.setDebug(true);
        }

        try {
            // login
            OpenOCredentials creds = OpenOCommandUtils.fromParameters(this.getParameters());
            this.authClient = new OpenOAuthClient(creds, this.getResult().isDebug());

            if (!this.openOservice.isNoAuth()
                    && !"true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_OUTPUT_NO_AUTH).getValue())) {
                this.authClient.login();
            }

            // execute
            this.run();

            // logout
            if (!this.openOservice.isNoAuth()
                    && !"true".equals(paramMap.get(Constants.DEFAULT_PARAMETER_OUTPUT_NO_AUTH).getValue())) {
                this.authClient.logout();
            }

            if (this.cmdResult.isDebug()) {
                this.cmdResult.setDebugInfo(this.authClient.getDebugInfo());
            }
        } catch (OpenOCommandException e) {
            if (this.cmdResult.isDebug()) {
                this.cmdResult.setDebugInfo(this.authClient.getDebugInfo());
            }
            throw e;
        }

        return this.cmdResult;
    }

    /*
     * Each command implements run method to executing the command.
     *
     */
    protected abstract void run() throws OpenOCommandException;

    /*
     * Get my service base path (endpoint).
     */
    protected String getBasePath() throws OpenOCommandException {
        return this.authClient.getServiceBasePath(this.getService());
    }

    protected String getAuthToken() {
        return this.authClient.getAuthToken();
    }

    /**
     * Returns the service service version it supports.
     *
     * @return version
     */
    public String printVersion() {
        return this.getService().toString();
    }

    /**
     * Provides help message for this command.
     *
     * @return help message
     * @throws OpenOCommandHelpFailed
     *             Failed to execute Help command.
     */
    public String printHelp() throws OpenOCommandHelpFailed {
        return OpenOCommandUtils.help(this);
    }
    // (mrkanag) Add toString for all command, parameter, result, etc objects in JSON format
}
