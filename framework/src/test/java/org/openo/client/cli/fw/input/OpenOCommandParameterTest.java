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

package org.openo.client.cli.fw.input;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandInvalidParameterValue;
import org.openo.client.cli.fw.error.OpenOCommandParameterMissing;

public class OpenOCommandParameterTest {

    @Test
    public void parameterObjTest() throws OpenOCommandInvalidParameterValue {
        OpenOCommandParameter param = new OpenOCommandParameter();
        param.setDefaultValue("defaultValue");
        param.setDescription("description");
        param.setLongOption("longOption");
        param.setName("name");
        param.setOptional(true);
        param.setParameterType(ParameterType.JSON);
        param.setSecured(false);
        param.setShortOption("shortOption");
        param.setValue("value");

        assertTrue(param.getDefaultValue().equals("defaultValue") && param.getDescription().equals("description")
                && param.getLongOption().equals("longOption") && param.getName().equals("name")
                && param.getShortOption().equals("shortOption") && param.getValue().equals("value") && param.isOptional()
                && !param.isSecured() && param.getParameterType().equals(ParameterType.JSON));
    }

    @Test
    public void parameterEnvDefaultValueObjTest() {
        OpenOCommandParameter param = new OpenOCommandParameter();
        param.setDefaultValue("${DAFAULT_VALUE}");
        boolean isDefaultValueAnEnv = param.isDefaultValueAnEnv();
        assertTrue(isDefaultValueAnEnv);

        String envValue = param.getEnvVarNameFromDefaultValue();

        assertTrue("DAFAULT_VALUE".equals(envValue));
    }

    @Test
    public void parameterValidateTest() {
        OpenOCommandParameter param = new OpenOCommandParameter();
        param.setOptional(false);
        param.setValue("");
        param.setDefaultValue("");
        param.setParameterType(ParameterType.STRING);
        try {
            param.validate();
        } catch (OpenOCommandParameterMissing | OpenOCommandInvalidParameterValue e) {
            assertTrue("0x0015::Parameter null is mandatory".equals(e.getMessage()));
        }
    }

}
