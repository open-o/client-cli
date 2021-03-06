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

package org.openo.client.cli.fw.output;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.client.cli.fw.input.ParameterType;

import java.util.Collections;

public class OpenOCommandResultAttributeScopeTest {
    @Test
    public void openOCommandResultAttributeTest() {
        OpenOCommandResultAttribute att = new OpenOCommandResultAttribute();
        att.setDescription("description");
        att.setName("name");
        att.setScope(OpenOCommandResultAttributeScope.LONG);
        att.setSecured(true);
        att.setType(ParameterType.LONG);
        att.setValues(Collections.emptyList());
        assertTrue("description".equals(att.getDescription()) && "name".equals(att.getName())
                && OpenOCommandResultAttributeScope.LONG.equals(att.getScope())
                && ParameterType.LONG.equals(att.getType()) && att.getValues().isEmpty());
    }

}
