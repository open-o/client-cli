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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandHelpFailed;
import org.openo.client.cli.fw.error.OpenOCommandInvalidParameterType;
import org.openo.client.cli.fw.error.OpenOCommandInvalidPrintDirection;
import org.openo.client.cli.fw.error.OpenOCommandInvalidRegistration;
import org.openo.client.cli.fw.error.OpenOCommandInvalidResultAttributeScope;
import org.openo.client.cli.fw.error.OpenOCommandNotFound;
import org.openo.client.cli.fw.error.OpenOCommandRegistrationFailed;

public class OpenOCommandRegistrarTest {

    OpenOCommandRegistrar registerar;

    @Before
    public void setup() throws OpenOCommandInvalidRegistration {
        registerar = OpenOCommandRegistrar.getRegistrar();
    }

    @Test
    public void registerTest() throws OpenOCommandInvalidRegistration, OpenOCommandNotFound,
            OpenOCommandRegistrationFailed, OpenOCommandInvalidParameterType, OpenOCommandInvalidPrintDirection,
            OpenOCommandInvalidResultAttributeScope {

        OpenOCommand test = new OpenOCommandTest();
        Class<OpenOCommand> cmd = (Class<OpenOCommand>) test.getClass();
        registerar.register("Test", cmd);
        OpenOCommand cc = registerar.get("Test");
        assertTrue(cmd == cc.getClass());

    }

    @Test
    public void openOCommandNotFoundTest()
            throws OpenOCommandRegistrationFailed, OpenOCommandInvalidParameterType, OpenOCommandInvalidPrintDirection,
            OpenOCommandInvalidResultAttributeScope, OpenOCommandInvalidRegistration {
        try {
            registerar = OpenOCommandRegistrar.getRegistrar();
            registerar.get("Test1");
            fail("This should have thrown an exception");
        } catch (OpenOCommandNotFound e) {
            assertEquals(e.getMessage(), "0x0011::Command Test1 is not registered");
        }
    }

    @Test
    public void openOCommandRegistrationFailedTest()
            throws OpenOCommandNotFound, OpenOCommandInvalidParameterType, OpenOCommandInvalidPrintDirection,
            OpenOCommandInvalidResultAttributeScope, OpenOCommandInvalidRegistration {

        OpenOCommand com = new OpenOCommand() {

            @Override
            protected void run() throws OpenOCommandException {

            }

        };
        Class<OpenOCommand> cmd = (Class<OpenOCommand>) com.getClass();
        try {
            registerar.register("Test2", cmd);
            registerar.get("Test2");
            fail("This should have thrown an exception");
        } catch (OpenOCommandRegistrationFailed e) {
            assertTrue(e.getMessage().contains("0x0018::Command Test2 is failed to register"));
        }
    }

    @Test
    public void helpTest() throws OpenOCommandHelpFailed {
        String help = registerar.getHelp();
        assertNotNull(help);
    }

    @Test
    public void versionTest() throws OpenOCommandHelpFailed {
        String version = registerar.getVersion();
        assertNotNull(version);
    }
}

@OpenOCommandSchema(name = OpenOCommandTest.CMD_NAME, schema = "sample-test-schema.yaml")
class OpenOCommandTest extends OpenOCommand {

    public OpenOCommandTest() {

    }

    public static final String CMD_NAME = "test";

    protected void run() throws OpenOCommandException {

    }

}
