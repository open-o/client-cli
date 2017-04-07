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

import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.schema.ValidateSchemaTest;

public class OpenOSchemaValidateCommandTest {

    @Test
    public void validateSchemaCommandTest1() throws OpenOCommandException {
        OpenOSchemaValidateCommand cmd = new OpenOSchemaValidateCommand();
        cmd.initializeSchema("schema-validate.yaml");
        for (OpenOCommandParameter param : cmd.getParameters()) {
            if ("openo-username".equals(param.getName())) {
                param.setValue("test");
            } else if ("openo-password".equals(param.getName())) {
                param.setValue("test");
            } else if ("msb-url".equals(param.getName())) {
                param.setValue("test-url");
            } else if ("schema-location".equals(param.getName())) {
                param.setValue("schema-validate-pass.yaml");
            } else if ("internal-schema".equals(param.getName())) {
                param.setValue("true");
            }
        }
        cmd.execute();
    }

    @Test
    public void validateSchemaCommandTest2() throws OpenOCommandException {
        OpenOSchemaValidateCommand cmd = new OpenOSchemaValidateCommand();
        cmd.initializeSchema("schema-validate.yaml");
        for (OpenOCommandParameter param : cmd.getParameters()) {
            if ("openo-username".equals(param.getName())) {
                param.setValue("test");
            } else if ("openo-password".equals(param.getName())) {
                param.setValue("test");
            } else if ("msb-url".equals(param.getName())) {
                param.setValue("test-url");
            } else if ("schema-location".equals(param.getName())) {
                param.setValue(
                        ValidateSchemaTest.class.getClassLoader().getResource("schema-validate-pass.yaml").getFile());
            }
        }
        cmd.execute();
    }
}
