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
import org.openo.client.cli.fw.error.OpenOCommandNotFound;
import org.openo.client.cli.fw.error.OpenOCommandRegistrationFailed;

import java.io.File;
import java.net.URL;

public class OpenOCommandRegistrarTest {

    OpenOCommandRegistrar registerar;

    @Before
    public void setup() throws OpenOCommandException {
        registerar = OpenOCommandRegistrar.getRegistrar();
        createDir();
    }

    private void createDir() {
        URL url = OpenOCommandRegistrarTest.class.getClassLoader().getResource("openo-cli-schema");
        if (url != null) {
            String path = url.getPath();
            path = path.replaceFirst("openo-cli-schema", "data");
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            } else {
                File f1 = new File(path + "/external-schema.json");
                f1.delete();
            }
        }
    }

    @Test
    public void registerTest() throws OpenOCommandException {
        OpenOCommand test = new OpenOCommandTest();
        Class<OpenOCommand> cmd = (Class<OpenOCommand>) test.getClass();
        registerar.register("Test", cmd);
        OpenOCommand cc = registerar.get("Test");
        assertTrue(cmd == cc.getClass());

    }

    @Test
    // For Coverage
    public void cmdTestSchema() throws OpenOCommandException {
        OpenOCommand test = new OpenOCommandTest();
        Class<OpenOCommand> cmd = (Class<OpenOCommand>) test.getClass();
        registerar.register("Test", cmd);
        OpenOCommand cc = registerar.get("Test");
    }

    @Test
    public void openOCommandNotFoundTest() throws OpenOCommandException {
        try {
            registerar = OpenOCommandRegistrar.getRegistrar();
            registerar.get("Test1");
            fail("This should have thrown an exception");
        } catch (OpenOCommandNotFound e) {
            assertEquals(e.getMessage(), "0x0011::Command Test1 is not registered");
        }
    }

    @Test
    public void openOCommandRegistrationFailedTest() throws OpenOCommandException {

        @OpenOCommandSchema(name = "Test2", schema = "sample-test-schema.yaml")
        class Test extends OpenOCommand {

            @Override
            protected void run() throws OpenOCommandException {

            }

        }

        OpenOCommand com = new Test();
        Class<OpenOCommand> cmd = (Class<OpenOCommand>) com.getClass();
        try {
            registerar.register("Test2", cmd);
            registerar.get("Test2");
            fail("This should have thrown an exception");
        } catch (OpenOCommandRegistrationFailed e) {
            assertTrue(e.getMessage().contains("0x0018::Command Test2 is failed to register"));
        }
    }

    @Test(expected = OpenOCommandHelpFailed.class)
    // For coverage
    public void helpTestException() throws OpenOCommandException {
        OpenOCommand test = new OpenOCommandTest1();
        Class<OpenOCommand> cmd = (Class<OpenOCommand>) test.getClass();
        registerar = new OpenOCommandRegistrar();
        registerar.register("test1", cmd);
        String help = registerar.getHelp();
        assertNotNull(help);
    }

    @Test
    public void helpTest() throws OpenOCommandException {
        String help = registerar.getHelp();
        assertNotNull(help);
    }

    @Test
    public void versionTest() throws OpenOCommandHelpFailed {
        String version = registerar.getVersion();
        assertNotNull(version);
    }

    @Test
    public void listTest() {
        registerar.listCommands();
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

@OpenOCommandSchema(name = OpenOCommandTest1.CMD_NAME, schema = "test-schema.yaml")
class OpenOCommandTest1 extends OpenOCommand {

    public OpenOCommandTest1() {

    }

    public static final String CMD_NAME = "test1";

    protected void run() throws OpenOCommandException {

    }

}
