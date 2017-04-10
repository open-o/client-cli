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

package org.openo.client.cli.cmd.sample;

import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.OpenOCommandSchema;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;

/**
 * This command helps to test the Command functionalities.
 *
 */
@OpenOCommandSchema(name = "sample-test", schema = "sample-test-schema.yaml")
public class OpenOCommandSample extends OpenOCommand {

    public OpenOCommandSample() {
        this(true);
    }

    public OpenOCommandSample(boolean isInit) {
        this.isInitialzied = isInit;
    }

    public boolean failCase = false;

    @Override
    protected void run() throws OpenOCommandException {
        if (this.failCase)
            throw new OpenOCommandExecutionFailed("Test case to fail");
    }

}