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

package org.openo.client.cli.fw.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.OpenOCommandSchema;
import org.openo.client.cli.fw.ad.OpenOCredentials;
import org.openo.client.cli.fw.cmd.OpenOHttpCommand;
import org.openo.client.cli.fw.cmd.OpenOSwaggerCommand;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandHttpInvalidResponseBody;
import org.openo.client.cli.fw.error.OpenOCommandInvalidParameterType;
import org.openo.client.cli.fw.error.OpenOCommandInvalidPrintDirection;
import org.openo.client.cli.fw.error.OpenOCommandInvalidResultAttributeScope;
import org.openo.client.cli.fw.error.OpenOCommandInvalidSchema;
import org.openo.client.cli.fw.error.OpenOCommandInvalidSchemaVersion;
import org.openo.client.cli.fw.error.OpenOCommandParameterNameConflict;
import org.openo.client.cli.fw.error.OpenOCommandParameterNotFound;
import org.openo.client.cli.fw.error.OpenOCommandParameterOptionConflict;
import org.openo.client.cli.fw.error.OpenOCommandSchemaNotFound;
import org.openo.client.cli.fw.http.HttpInput;
import org.openo.client.cli.fw.http.HttpResult;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.input.ParameterType;
import org.openo.client.cli.fw.run.OpenOCommandExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpenOCommandUtilsTest {

    @Test(expected = OpenOCommandSchemaNotFound.class)
    public void openOCommandUtilsInputStreamNullTest() throws OpenOCommandException {
        OpenOCommandUtils.validateSchemaVersion("sample-test1-schema-http1.yaml", "1.0");
    }

    @Test
    public void openOCommandUtilsInputStreamNotNullTest() throws OpenOCommandException {
        Map<String, ?> map = OpenOCommandUtils.validateSchemaVersion("sample-test1-schema-http.yaml", "1.0");
        assertTrue(map != null);
    }

    @Test
    public void externalSchemaTest() {
        ExternalSchema schema = new ExternalSchema();
        schema.setCmdName("cmdName");
        schema.setSchemaName("schemaName");
        schema.setVersion("version");

        assertTrue("cmdName".equals(schema.getCmdName()) && "schemaName".equals(schema.getSchemaName())
                && "version".equals(schema.getVersion()));
    }

    @Test
    public void schemaFileNotFoundTest() throws OpenOCommandException {

        Map<String, ?> map = OpenOCommandUtils.validateSchemaVersion("sample-test-schema.yaml", "1.0");
        assertTrue(map.size() > 0);
    }

    @Test
    @Ignore
    public void invalidSchemaFileTest() throws OpenOCommandException {
        Map<String, ?> map = null;
        try {
            map = OpenOCommandUtils.validateSchemaVersion("sample-test-schema1.yaml", "1.0");
        } catch (OpenOCommandInvalidSchemaVersion e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        } catch (OpenOCommandInvalidSchema e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        } catch (OpenOCommandSchemaNotFound e) {
            assertEquals("0x0019::Command schema sample-test-schema1.yaml is not found", e.getMessage());
        }
    }

    @Test
    public void validateWrongSchemaVersionTest() throws OpenOCommandException {
        Map<String, ?> map = null;
        try {
            map = OpenOCommandUtils.validateSchemaVersion("sample-test-invalid-schema.yaml", "1.0");
        } catch (OpenOCommandInvalidSchemaVersion e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        } catch (OpenOCommandInvalidSchema e) {
            assertTrue(e.getMessage().contains("0x0007::Command schema sample-test-invalid-schema.yaml is invalid"));
        } catch (OpenOCommandSchemaNotFound e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        }
    }

    @Test
    public void validateSchemaVersionTest() throws OpenOCommandException {
        Map<String, ?> map = null;
        try {
            map = OpenOCommandUtils.validateSchemaVersion("sample-test-schema.yaml", "1.1");
        } catch (OpenOCommandInvalidSchemaVersion e) {
            assertEquals("0x0008::Command schema openo_cmd_schema_version 1.0 is invalid or missing", e.getMessage());
        } catch (OpenOCommandInvalidSchema e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        } catch (OpenOCommandSchemaNotFound e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        }
    }

    @Test
    public void loadOpenoCommandSchemaWithOutDefaultTest() throws OpenOCommandException {
        OpenOCommand cmd = new OpenOCommandSample();
        OpenOCommandUtils.loadSchema(cmd, "sample-test-schema.yaml", false);
        assertTrue("sample-test".equals(cmd.getName()) && cmd.getParameters().size() == 9);
    }

    @Test(expected = OpenOCommandParameterNameConflict.class)
    public void loadOpenoCommandSchemaWithDuplicateNameTest() throws OpenOCommandException {
        OpenOCommand cmd = new OpenOCommandSample();
        OpenOCommandUtils.loadSchema(cmd, "sample-test-invalid-schema-duplicate-name.yaml", false);
    }

    @Test(expected = OpenOCommandParameterOptionConflict.class)
    public void loadOpenoCommandSchemaWithDuplicateShortOptionTest() throws OpenOCommandException {
        OpenOCommand cmd = new OpenOCommandSample();
        OpenOCommandUtils.loadSchema(cmd, "sample-test-invalid-schema-duplicate-shortoption.yaml", false);
    }

    @Test(expected = OpenOCommandParameterOptionConflict.class)
    public void loadOpenoCommandSchemaWithDuplicateLongOptionTest() throws OpenOCommandException {
        OpenOCommand cmd = new OpenOCommandSample();
        OpenOCommandUtils.loadSchema(cmd, "sample-test-invalid-schema-duplicate-longoption.yaml", false);
    }

    @Test
    public void loadOpenoCommandSchemaWithDefaultTest() throws OpenOCommandException {
        OpenOCommand cmd = new OpenOCommandSample();
        OpenOCommandUtils.loadSchema(cmd, "sample-test-schema.yaml", true);
        assertTrue("sample-test".equals(cmd.getName()) && cmd.getParameters().size() > 9);

        for (OpenOCommandParameter com : cmd.getParameters()) {
            com.setValue("value");
        }

        OpenOCredentials cre = OpenOCommandUtils.fromParameters(cmd.getParameters());
        assertTrue(cre != null);
        Map<String, OpenOCommandParameter> map = OpenOCommandUtils.getInputMap(cmd.getParameters());
        assertTrue(map.size() == 18);
    }

    @Test
    public void loadSwaggerBasedSchemaExceptionTest() throws OpenOCommandException {
        OpenOSwaggerCommand cmd = new OpenOSwaggerBasedCommandSample();
        try {
            OpenOCommandUtils.loadSchema(cmd, "sample-test-schema.yaml");
        } catch (OpenOCommandInvalidSchema e) {
            assertTrue(e.getMessage().contains("0x0007::Command schema sample-test-schema.yaml is invalid"));
        }
    }

    @Test
    public void loadSwaggerBasedSchemaTest() throws OpenOCommandException {
        OpenOSwaggerCommand cmd = new OpenOSwaggerBasedCommandSample();
        try {
            OpenOCommandUtils.loadSchema(cmd, "sample-test-schema-swagger.yaml");
            OpenOCommandExecutor exe = cmd.getExecutor();
            assertTrue(exe != null);
        } catch (OpenOCommandInvalidSchema e) {
            assertTrue(e.getMessage().contains("0x0007::Command schema sample-test-schema.yaml is invalid"));
        }
    }

    @Test
    public void loadHttpBasedSchemaExceptionTest() throws OpenOCommandException {
        OpenOHttpCommand cmd = new OpenOHttpCommandSample();
        cmd.setName("sample-test-http");
        try {
            OpenOCommandUtils.loadSchema(cmd, "sample-test-schema.yaml");
        } catch (OpenOCommandParameterNameConflict | OpenOCommandParameterOptionConflict
                | OpenOCommandInvalidParameterType | OpenOCommandInvalidPrintDirection
                | OpenOCommandInvalidResultAttributeScope | OpenOCommandSchemaNotFound | OpenOCommandInvalidSchema
                | OpenOCommandInvalidSchemaVersion e) {
            assertTrue(e.getMessage().contains("0x0007::Command schema sample-test-schema.yaml is invalid"));
        }
    }

    @Test
    public void loadHttpBasedSchemaTest() throws OpenOCommandException {
        OpenOHttpCommand cmd = new OpenOHttpCommandSample();
        cmd.setName("sample-test-http");
        try {
            OpenOCommandUtils.loadSchema(cmd, "sample-test-schema-http.yaml");
            assertTrue(cmd.getSuccessStatusCodes().size() == 3);
        } catch (OpenOCommandParameterNameConflict | OpenOCommandParameterOptionConflict
                | OpenOCommandInvalidParameterType | OpenOCommandInvalidPrintDirection
                | OpenOCommandInvalidResultAttributeScope | OpenOCommandSchemaNotFound | OpenOCommandInvalidSchema
                | OpenOCommandInvalidSchemaVersion e) {
            fail("Test should not have thrown this exception : " + e.getMessage());
        }
    }

    @Test
    public void helpCommandTest() throws IOException, OpenOCommandException {
        OpenOCommand cmd = new OpenOCommandSample();
        OpenOCommandUtils.loadSchema(cmd, "sample-test-schema.yaml", true);

        String actualResult = OpenOCommandUtils.help(cmd);

        String expectedHelp = FileUtil.loadResource("sample-cmd-test-help.txt");

        assertTrue(expectedHelp.equals(actualResult));

    }

    @Test
    public void findOpenOCommandsTest() {
        List<Class<OpenOCommand>> cmds = OpenOCommandUtils.findOpenOCommands();
        assertTrue(cmds.size() == 1);
    }

    @Test
    public void sortTest() {
        Set<String> set = new HashSet<String>();
        set.add("dbvc");
        set.add("bbvcb");
        set.add("aaa");
        set.add("c");
        set.add("z");
        List<String> list = OpenOCommandUtils.sort(set);
        assertEquals("[aaa, bbvcb, c, dbvc, z]", list.toString());
    }

    @Test
    public void jsonFlattenTest() {
        List<String> list = Arrays.asList(new String[] { "{\"menu1\": {\"id\": \"file1\",\"value\": \"File1\"}}" });
        List<String> list1 = OpenOCommandUtils.jsonFlatten(list);
        String expected = "[{\"menu1\":{\"id\":\"file1\",\"value\":\"File1\"}}]";
        assertEquals(expected, list1.toString());

    }

    @Test
    public void formMethodNameFromAttributeTest() {

        String str = "";
        String name = OpenOCommandUtils.formMethodNameFromAttributeName(str, "test");

        assertEquals("", name);

        str = null;
        name = OpenOCommandUtils.formMethodNameFromAttributeName(str, "test");

        assertEquals(null, name);

        str = "test-get";
        name = OpenOCommandUtils.formMethodNameFromAttributeName(str, "");
        assertEquals("TestGet", name);

    }

    @Test
    public void populateParametersTest() throws OpenOCommandException {

        HttpInput input = new HttpInput();
        input.setBody("body");
        input.setMethod("method");
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("key2", "${value2}");
        input.setReqHeaders(mapHead);
        Map<String, String> query = new HashMap<>();
        query.put("key3", "${value3}");
        input.setReqQueries(query);
        input.setUri("uri");

        Map<String, OpenOCommandParameter> params = new HashMap<>();
        OpenOCommandParameter param = new OpenOCommandParameter();
        param.setDefaultValue("defaultValue2");
        param.setParameterType(ParameterType.STRING);
        params.put("value2", param);
        OpenOCommandParameter param1 = new OpenOCommandParameter();
        param1.setDefaultValue("defaultValue3");
        param1.setParameterType(ParameterType.STRING);
        params.put("value3", param1);

        HttpInput input1 = OpenOCommandUtils.populateParameters(params, input);
        String expected = "\nURL: uri\nMethod: method\nRequest Queries: {key3=defaultValue3}\n"
                + "Request Body: body\nRequest Headers: {key2=defaultValue2}\nRequest Cookies: {}";
        assertEquals(expected, input1.toString());

        input.setBody("${body}");

        HttpInput input2 = null;
        try {
            input2 = OpenOCommandUtils.populateParameters(params, input);
        } catch (OpenOCommandParameterNotFound e) {
            assertEquals("0x0026::Command input parameter body is not valid", e.getMessage());
        }

    }

    @Test
    public void populateOutputsTest() throws OpenOCommandException {
        HttpResult output = new HttpResult();
        output.setBody("{\"serviceName\":\"test\",\"version\":\"v1\",\"url\":\"/api/test/v1\",\"protocol\":\"REST\"}");
        Map<String, String> mapHead = new HashMap<>();
        mapHead.put("head1", "value1");
        output.setRespHeaders(mapHead);
        output.setStatus(0);

        Map<String, String> params = new HashMap<>();
        params.put("head", "$h{head1}");
        params.put("body", "$b{$.serviceName}");

        Map<String, ArrayList<String>> input1 = OpenOCommandUtils.populateOutputs(params, output);
        assertEquals("{head=[value1], body=[test]}", input1.toString());

        params.put("body", "$b{{$.serviceName}");
        try {
            input1 = OpenOCommandUtils.populateOutputs(params, output);
        } catch (OpenOCommandHttpInvalidResponseBody e) {
            assertEquals(
                    "0x0028::Http response body does not have json entry {$.serviceName, Missing property in path $['{$']",
                    e.getMessage());
        }

    }

    @OpenOCommandSchema(name = "sample-test", schema = "sample-test-schema.yaml")
    class OpenOCommandSample extends OpenOCommand {
        @Override
        protected void run() throws OpenOCommandException {
        }
    }

    @OpenOCommandSchema(name = "sample-swagger-test", schema = "sample-test-schema-swagger.yaml")
    class OpenOSwaggerBasedCommandSample extends OpenOSwaggerCommand {

        @Override
        protected void run() throws OpenOCommandException {
        }
    }

    @OpenOCommandSchema(name = "sample-http-test", schema = "sample-test-schema-http.yaml")
    class OpenOHttpCommandSample extends OpenOHttpCommand {

        @Override
        protected void run() throws OpenOCommandException {
        }
    }
}
