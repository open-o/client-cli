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
import static org.junit.Assert.fail;

import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandInvalidResultAttributeScope;

public class OpenOCommandResultAttributeTest {
    @Test
    public void paramTypeGetTest() {

        try {
            assertTrue(OpenOCommandResultAttributeScope.LONG.equals(OpenOCommandResultAttributeScope.get("long"))
                    && OpenOCommandResultAttributeScope.SHORT.equals(OpenOCommandResultAttributeScope.get("short")));
        } catch (OpenOCommandInvalidResultAttributeScope e) {
            fail("Shouldn't have thrown this exception : " + e.getMessage());
        }

        try {
            OpenOCommandResultAttributeScope.get("name");
        } catch (OpenOCommandInvalidResultAttributeScope e) {
            assertTrue("0x0006::Result atrribute name is invalid".equals(e.getMessage()));
        }

    }

}
