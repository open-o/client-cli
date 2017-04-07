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

package org.openo.client.cli.fw.cmd;

import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.conf.Constants;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.http.HttpInput;
import org.openo.client.cli.fw.http.HttpResult;
import org.openo.client.cli.fw.output.OpenOCommandResultAttribute;
import org.openo.client.cli.fw.utils.OpenOCommandUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenO Command.
 *
 */
public class OpenOHttpCommand extends OpenOCommand {

    private HttpInput input = new HttpInput();

    private List<Integer> successStatusCodes = new ArrayList<>();

    private Map<String, String> resultMap = new HashMap<>();

    public void setInput(HttpInput input) {
        this.input = input;
    }

    @Override
    public String getSchemaVersion() {
        return Constants.OPENO_CMD_SCHEMA_VERSION_VALUE;
    }

    public void setSuccessStatusCodes(List<Integer> successStatusCodes) {
        this.successStatusCodes = successStatusCodes;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    public HttpInput getInput() {
        return input;
    }

    public List<Integer> getSuccessStatusCodes() {
        return successStatusCodes;
    }

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    @Override
    protected void initializeProfileSchema() throws OpenOCommandException {
        OpenOCommandUtils.loadSchema(this, this.getSchemaName());
    }

    @Override
    protected void run() throws OpenOCommandException {
        HttpInput httpInput = OpenOCommandUtils.populateParameters(this.getParametersMap(), this.getInput());
        httpInput.setUri(this.authClient.getServiceBasePath(this.getService()) + httpInput.getUri());

        HttpResult output = this.authClient.run(httpInput);

        this.getResult().setOutput(output);
        if (!this.getSuccessStatusCodes().contains(output.getStatus())) {
            throw new OpenOCommandExecutionFailed(this.getName(), output.getBody(), output.getStatus());
        }

        Map<String, ArrayList<String>> results = OpenOCommandUtils.populateOutputs(this.getResultMap(), output);

        for (OpenOCommandResultAttribute attr : this.getResult().getRecords()) {
            attr.setValues(results.get(attr.getName()));
        }
    }
}
