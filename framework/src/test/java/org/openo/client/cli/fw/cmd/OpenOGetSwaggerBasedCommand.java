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

import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.error.OpenOCommandExecutorInfoMissing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Helps to make get rest calls on top of swagger generated java client. For example following one shows how MSB service
 * list is achieved here exec: ... method: getMicroService, [input param name given under Parameters]
 *
 */
public class OpenOGetSwaggerBasedCommand extends OpenOSwaggerCommand {

    @Override
    protected void run() throws OpenOCommandException {
        if (this.getExecutor() == null) {
            throw new OpenOCommandExecutorInfoMissing(this.getName());
        }

        try {
            // Initialize client
            Class clientCls = Class.forName(this.getExecutor().getClient());
            Object client = clientCls.getConstructor().newInstance();
            this.initializeApiClient(client);

            // Initialize api
            Class apiCls = Class.forName(this.getExecutor().getApi());
            Object api = apiCls.getConstructor(clientCls).newInstance(client);

            // invoke method
            List<String> methodTokens = Arrays.asList(this.getExecutor().getMethod().split(","));

            Method method = api.getClass().getMethod(methodTokens.get(0), String.class);
            Object result = method.invoke(api, this.getParametersMap().get(methodTokens.get(1)).getValue());

            // initialize result
            this.initializeResult(result);

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new OpenOCommandExecutionFailed(this.getName(), e.getMessage());
        } catch (OpenOCommandException e) {
            throw e;
        } catch (Exception e) {
            try {
                Class execCls = Class.forName(this.getExecutor().getException());
                Method execMethod = execCls.getClass().getMethod("getCode");
                if (execCls.isInstance(e)) {
                    throw new OpenOCommandExecutionFailed(this.getName(), e.getMessage(),
                            (Integer) execMethod.invoke(e));
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | ClassNotFoundException | NoSuchMethodException | SecurityException e1) {
                throw new OpenOCommandExecutionFailed(this.getName(), e1.getMessage());
            }
            throw new OpenOCommandExecutionFailed(this.getName(), e.getMessage());
        }
    }
}
