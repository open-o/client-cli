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

package org.openo.client.cli.main.utils;

import org.junit.Assert;
import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.input.ParameterType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenOCliUtilsTest {

    @SuppressWarnings("static-access")
    @Test
    public void testpopulateParamsLong() throws OpenOCommandException {
        OpenOCommandParameter param1 = new OpenOCommandParameter();
        param1.setLongOption("openo-username");
        param1.setName("openo-username");
        param1.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param2 = new OpenOCommandParameter();
        param2.setLongOption("openo-password");
        param2.setName("openo-password");
        param2.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param3 = new OpenOCommandParameter();
        param3.setLongOption("msb-url");
        param3.setName("msb-url");
        param3.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param4 = new OpenOCommandParameter();
        param4.setLongOption("string-param");
        param4.setName("string-param");
        param4.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param5 = new OpenOCommandParameter();
        param5.setLongOption("long-opt");
        param5.setName("long-opt");
        param5.setParameterType(ParameterType.STRING);

        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(param1);
        paramslist.add(param2);
        paramslist.add(param3);
        paramslist.add(param4);
        paramslist.add(param5);

        String[] args = new String[] { "sample-create", "--openo-username", "admin", "--openo-password", "123",
                "--msb-url", "a@b.com", "--string-param", "blah", "--long-opt", "10" };
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));
        List<String> expectedList = Arrays.asList(args);

        Assert.assertEquals("openo-username", expectedList.get(2), paramslist.get(4).getValue());
        Assert.assertEquals("openo-password", expectedList.get(4), paramslist.get(1).getValue());
        Assert.assertEquals("msb-url", expectedList.get(6), paramslist.get(0).getValue());
        Assert.assertEquals("string-param", expectedList.get(8), paramslist.get(2).getValue());
        Assert.assertEquals("long-opt", expectedList.get(10), paramslist.get(3).getValue());

    }

    @SuppressWarnings("static-access")
    @Test
    public void testpositionalargs() throws OpenOCommandException {
        OpenOCommandParameter paramargs = new OpenOCommandParameter();
        paramargs.setName("http://localhost:8082/file.txt");
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(paramargs);
        String[] args = new String[] { "positional-args", "http://localhost:8082/file.txt" };
        paramargs.setParameterType(ParameterType.STRING);
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));
        List<String> expectedList = Arrays.asList(args);
        Assert.assertEquals("positional-args", expectedList.get(1), paramslist.get(0).getValue());
    }

    @SuppressWarnings("static-access")
    @Test
    public void testboolparamslong() throws OpenOCommandException {
        OpenOCommandParameter boolparam = new OpenOCommandParameter();
        boolparam.setLongOption("bool");
        boolparam.setName("bool-param");
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(boolparam);
        String[] args = new String[] { "sample-create", "--bool" };

        boolparam.setParameterType(ParameterType.BOOL);
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));
        List<String> expectedList = Arrays.asList(args);
        Assert.assertNotNull(expectedList.get(1), paramslist.get(0).getValue());

    }

    @SuppressWarnings("static-access")
    @Test
    public void testboolparamsshort() throws OpenOCommandException {
        OpenOCommandParameter boolparam = new OpenOCommandParameter();
        boolparam.setShortOption("b");
        boolparam.setName("bool-param");
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(boolparam);
        String[] args = new String[] { "sample-create", "-b", };

        boolparam.setParameterType(ParameterType.BOOL);
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));
        List<String> expectedList = Arrays.asList(args);
        Assert.assertNotNull(expectedList.get(1), paramslist.get(0).getValue());
    }

    @Test
    public void testjsonparamsshort() throws OpenOCommandException {
        OpenOCommandParameter jsonparam = new OpenOCommandParameter();
        jsonparam.setShortOption("j");
        jsonparam.setName("json-param");
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(jsonparam);
        File resourcesDirectory = new File("src/test/resources/sampletest.json");
        String[] args = new String[] { "sample-create", "-j", "file:" + resourcesDirectory };
        jsonparam.setParameterType(ParameterType.JSON);
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));
        List<String> expectedList = Arrays.asList(args);
        Assert.assertNotNull(expectedList.get(1), paramslist.get(0).getValue());
    }

    @Test
    public void testjsonparamslong() throws OpenOCommandException {
        OpenOCommandParameter jsonparam = new OpenOCommandParameter();
        jsonparam.setLongOption("json-param");
        jsonparam.setName("json-param");
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(jsonparam);
        File resourcesDirectory = new File("src/test/resources/sampletest.json");
        String[] args = new String[] { "sample-create", "--json-param", "file:" + resourcesDirectory };
        jsonparam.setParameterType(ParameterType.JSON);
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));
        List<String> expectedList = Arrays.asList(args);
        Assert.assertNotNull(expectedList.get(1), paramslist.get(0).getValue());
    }

    @SuppressWarnings("static-access")
    @Test
    public void testpopulateParamsShort() throws OpenOCommandException {

        OpenOCommandParameter param1 = new OpenOCommandParameter();
        param1.setShortOption("u");
        param1.setName("openo-username");
        param1.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param2 = new OpenOCommandParameter();
        param2.setShortOption("p");
        param2.setName("openo-password");
        param2.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param3 = new OpenOCommandParameter();
        param3.setShortOption("r");
        param3.setName("msb-url");
        param3.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param4 = new OpenOCommandParameter();
        param4.setShortOption("c");
        param4.setName("string-param");
        param4.setParameterType(ParameterType.STRING);
        OpenOCommandParameter param5 = new OpenOCommandParameter();
        param5.setShortOption("l");
        param5.setName("long-opt");
        param5.setParameterType(ParameterType.STRING);

        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(param1);
        paramslist.add(param2);
        paramslist.add(param3);
        paramslist.add(param4);
        paramslist.add(param5);

        String[] args11 = new String[] { "sample-create", "-u", "admin", "-p", "123", "-r", "a@b.com", "-c", "blah",
                "-l", "10", };
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args11));

        List<String> expectedList = Arrays.asList(args11);

        Assert.assertEquals("u", expectedList.get(2), paramslist.get(4).getValue());
        Assert.assertEquals("-p", expectedList.get(4), paramslist.get(1).getValue());
        Assert.assertEquals("r", expectedList.get(6), paramslist.get(0).getValue());
        Assert.assertEquals("c", expectedList.get(8), paramslist.get(2).getValue());
        Assert.assertEquals("l", expectedList.get(10), paramslist.get(3).getValue());

    }

    @Test
    public void testArrayparamslong() throws OpenOCommandException {
        OpenOCommandParameter arrayval = new OpenOCommandParameter();
        arrayval.setLongOption("node-ip");
        arrayval.setName("node-ip");

        String[] args = new String[] { "sample-create", "--node-ip", "{}" };
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(arrayval);

        arrayval.setParameterType(ParameterType.ARRAY);
        OpenOCliUtils.populateParams(paramslist, Arrays.asList(args));

        List<String> expectedList = Arrays.asList(args);
        Assert.assertNotNull(expectedList.get(1), paramslist.get(0).getValue());
    }

}
