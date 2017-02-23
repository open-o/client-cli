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
import org.openo.client.cli.fw.error.OpenOCommandClientInitialzationFailed;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.error.OpenOCommandHttpFailure;
import org.openo.client.cli.fw.error.OpenOCommandResultInitialzationFailed;
import org.openo.client.cli.fw.error.OpenOCommandServiceNotFound;
import org.openo.client.cli.fw.output.OpenOCommandResultAttribute;
import org.openo.client.cli.fw.run.OpenOCommandExecutor;
import org.openo.client.cli.fw.utils.OpenOCommandUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class OpenOSwaggerCommand extends OpenOCommand {

    // TODO(mrkanag): change this different value
    public static final String OPENO_CMD_SCHEMA_VERSION_VALUE = "1.0"; // cli-swagger-

    public static final String EXECUTOR = "exec";

    private OpenOCommandExecutor executor;

    public OpenOCommandExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(OpenOCommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Initialize the given ApiClient object with AUTH token and base path.
     *
     * @param client
     *            api client
     * @throws OpenOCommandClientInitialzationFailed
     *             client initialization failed
     */
    protected <T> T initializeApiClient(T client) throws OpenOCommandClientInitialzationFailed {
        try {
            Method basePath = client.getClass().getMethod("setBasePath", String.class);
            basePath.invoke(client, this.getBasePath());

            if (this.getAuthToken() != null) {
                try {
                    Method apiKey = client.getClass().getMethod("setApiKey", String.class);
                    apiKey.invoke(client, this.getAuthToken());
                } catch (InvocationTargetException e) {
                    // TODO(mrkanag): once X-AUTH-TOKEN is available, raise exception here
                }
            }
            return client;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | OpenOCommandExecutionFailed | OpenOCommandServiceNotFound
                | OpenOCommandHttpFailure e) {
            throw new OpenOCommandClientInitialzationFailed(this.getName(), e.getMessage());
        }
    }

    protected <T> void initializeResult(T obj) throws OpenOCommandResultInitialzationFailed {
        this.getResult().setOutput(obj);
        if (obj instanceof List) {
            this.initializeListResult((List) obj);
        } else {
            this.initializeRow(obj);
        }
    }

    private <T> void initializeRow(T obj) throws OpenOCommandResultInitialzationFailed {
        for (OpenOCommandResultAttribute row : this.getResult().getRecords()) {
            String methodName = OpenOCommandUtils.formMethodNameFromAttributeName(row.getName(), "get");
            Method get;
            try {
                get = obj.getClass().getMethod(methodName);
                row.getValues().add(get.invoke(obj).toString());
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new OpenOCommandResultInitialzationFailed(this.getName(), e.getMessage());
            }
        }
    }

    protected <T> void initializeListResult(List<T> rows) throws OpenOCommandResultInitialzationFailed {
        this.getResult().setOutput(rows);
        for (T row : rows) {
            this.initializeRow(row);
        }
    }
}
