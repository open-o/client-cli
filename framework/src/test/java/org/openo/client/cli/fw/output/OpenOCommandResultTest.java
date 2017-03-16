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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.input.ParameterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenOCommandResultTest {

    @Test
    public void commandResultObjTest() {
        OpenOCommandResult res = new OpenOCommandResult();
        res.setDebugInfo("debugInfo");
        res.setIncludeSeparator(true);
        res.setIncludeTitle(true);
        res.setOutput("Output");
        res.setPrintDirection(PrintDirection.LANDSCAPE);
        res.setRecords(new ArrayList<OpenOCommandResultAttribute>());
        res.setScope(OpenOCommandResultAttributeScope.LONG);
        res.setType(ResultType.TABLE);

        assertTrue("debugInfo".equals(res.getDebugInfo()) && res.isIncludeSeparator()
                && "Output".equals(res.getOutput()) && PrintDirection.LANDSCAPE.equals(res.getPrintDirection())
                && res.getRecords().isEmpty() && OpenOCommandResultAttributeScope.LONG.equals(res.getScope())
                && ResultType.TABLE.equals(res.getType()));

    }

    @Test
    public void commandResultPrintLandscapeTableTest() throws OpenOCommandException {
        OpenOCommandResult res = new OpenOCommandResult();
        res.setDebugInfo("debugInfo");
        res.setIncludeSeparator(true);
        res.setIncludeTitle(true);
        res.setOutput("Output");
        res.setPrintDirection(PrintDirection.LANDSCAPE);

        OpenOCommandResultAttribute att = new OpenOCommandResultAttribute();
        att.setName("param");
        att.setDescription("description");
        att.setType(ParameterType.STRING);
        att.setValues(new ArrayList<String>(Arrays.asList(new String[] { "value" })));
        List<OpenOCommandResultAttribute> list = new ArrayList<OpenOCommandResultAttribute>();
        list.add(att);
        res.setRecords(list);
        res.setScope(OpenOCommandResultAttributeScope.LONG);
        res.setType(ResultType.TABLE);
        String expRes = "+--------+\n|param   |\n+--------+\n|value   |\n+--------+\n";
        String result = res.print();

        assertEquals(expRes, result);

    }

    @Test
    public void commandResultPrintLandscapeJsonTest() throws OpenOCommandException {
        OpenOCommandResult res = new OpenOCommandResult();
        res.setDebugInfo("debugInfo");
        res.setIncludeSeparator(true);
        res.setIncludeTitle(true);
        res.setOutput("Output");
        res.setPrintDirection(PrintDirection.LANDSCAPE);

        OpenOCommandResultAttribute att = new OpenOCommandResultAttribute();
        att.setName("param");
        att.setDescription("description");
        att.setType(ParameterType.JSON);
        att.setValues(
                new ArrayList<String>(Arrays.asList(new String[] { "{\"id\": \"0001\",\"value\": \"result\"}" })));
        List<OpenOCommandResultAttribute> list = new ArrayList<OpenOCommandResultAttribute>();
        list.add(att);
        res.setRecords(list);
        res.setScope(OpenOCommandResultAttributeScope.LONG);
        res.setType(ResultType.JSON);

        // Will be handled after the json print is implemented
        String result = res.print();
        // String expRes = "+--------+\n|param |\n+--------+\n|value
        // |\n+--------+\n";
        // assertEquals(expRes,result);

    }

    @Test
    @Ignore
    public void commandResultPrintLandscapeCsvTest() throws OpenOCommandException {
        OpenOCommandResult res = new OpenOCommandResult();
        res.setDebugInfo("debugInfo");
        res.setIncludeSeparator(true);
        res.setIncludeTitle(true);
        res.setOutput("Output");
        res.setPrintDirection(PrintDirection.LANDSCAPE);

        OpenOCommandResultAttribute att = new OpenOCommandResultAttribute();
        att.setName("param");
        att.setDescription("description");
        att.setType(ParameterType.STRING);
        att.setValues(new ArrayList<String>(Arrays.asList(new String[] { "value" })));
        List<OpenOCommandResultAttribute> list = new ArrayList<OpenOCommandResultAttribute>();
        list.add(att);
        OpenOCommandResultAttribute a1 = new OpenOCommandResultAttribute();
        a1.setName("param1");
        a1.setDescription("description1");
        a1.setType(ParameterType.STRING);
        a1.setValues(new ArrayList<String>(Arrays.asList(new String[] { "value1" })));

        list.add(a1);
        res.setRecords(list);
        res.setScope(OpenOCommandResultAttributeScope.LONG);
        res.setType(ResultType.CSV);

        String expRes = "param,param1\r\n";
        String result = res.print();
        assertEquals(expRes, result);

    }

    @Test
    @Ignore
    public void commandResultPrintPortraitCsvTest() throws OpenOCommandException {
        OpenOCommandResult res = new OpenOCommandResult();
        res.setDebugInfo("debugInfo");
        res.setIncludeSeparator(true);
        res.setIncludeTitle(true);
        res.setOutput("Output");
        res.setPrintDirection(PrintDirection.PORTRAIT);

        OpenOCommandResultAttribute att = new OpenOCommandResultAttribute();
        att.setName("param");
        att.setDescription("description");
        att.setType(ParameterType.STRING);
        att.setValues(new ArrayList<String>(Arrays.asList(new String[] { "value" })));
        List<OpenOCommandResultAttribute> list = new ArrayList<OpenOCommandResultAttribute>();
        list.add(att);
        OpenOCommandResultAttribute a1 = new OpenOCommandResultAttribute();
        a1.setName("param1");
        a1.setDescription("description1");
        a1.setType(ParameterType.STRING);
        a1.setValues(new ArrayList<String>(Arrays.asList(new String[] { "value1" })));

        list.add(a1);
        res.setRecords(list);
        res.setScope(OpenOCommandResultAttributeScope.LONG);
        res.setType(ResultType.CSV);
        String expRes = "property,value\r\nparam,value\r\n";
        String result = res.print();
        assertEquals(expRes, result);
    }

    @Test
    public void commandResultPrintPortraitTableTest() throws OpenOCommandException {
        OpenOCommandResult res = new OpenOCommandResult();
        res.setDebugInfo("debugInfo");
        res.setIncludeSeparator(true);
        res.setIncludeTitle(true);
        res.setOutput("Output");
        res.setPrintDirection(PrintDirection.PORTRAIT);

        OpenOCommandResultAttribute att = new OpenOCommandResultAttribute();
        att.setName("param");
        att.setDescription("description");
        att.setType(ParameterType.STRING);
        att.setValues(new ArrayList<String>(Arrays.asList(new String[] { "value" })));

        List<OpenOCommandResultAttribute> list = new ArrayList<OpenOCommandResultAttribute>();
        list.add(att);
        res.setRecords(list);
        res.setScope(OpenOCommandResultAttributeScope.LONG);
        res.setType(ResultType.TABLE);
        String expRes = "+----------+--------+\n|property  |value   |\n+----------+--------+"
                + "\n|param     |value   |\n+----------+--------+\n";

        String result = res.print();

        assertEquals(expRes, result);

    }

}
