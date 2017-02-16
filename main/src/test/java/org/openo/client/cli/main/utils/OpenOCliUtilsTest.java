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
import org.junit.Before;
import org.junit.Test;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.main.error.OpenOCliArgumentValueMissing;
import org.openo.client.cli.main.error.OpenOCliInvalidArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OpenOCliUtilsTest {

    private OpenOCliUtils openOCliUtils;

    @Before
    public void setUp() {
        openOCliUtils = new OpenOCliUtils();
    }

    @Test
    public void testpopulateParamsLong() throws OpenOCliArgumentValueMissing, OpenOCliInvalidArgument  {
        OpenOCommandParameter param1 = new OpenOCommandParameter();
        param1.setLongOption("openo-username");
        param1.setName("openo-username");
        OpenOCommandParameter param2 = new OpenOCommandParameter();
        param2.setLongOption("openo-password");
        param2.setName("openo-password");
        OpenOCommandParameter param3 = new OpenOCommandParameter();
        param3.setLongOption("msb-url");
        param3.setName("msb-url");
        OpenOCommandParameter param4 = new OpenOCommandParameter();
        param4.setLongOption("string-param");
        param4.setName("string-param");
        OpenOCommandParameter param5 = new OpenOCommandParameter();
        param5.setLongOption("long-opt");
        param5.setName("long-opt");
        OpenOCommandParameter param6 = new OpenOCommandParameter();
        param6.setLongOption("yaml-param");
        param6.setName("yaml-param");
        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(param1);
        paramslist.add(param2);
        paramslist.add(param3);
        paramslist.add(param4);
        paramslist.add(param5);
        paramslist.add(param6);

        String[] args = new String[] {"bool", "--openo-username", "admin", "--openo-password", "123", "--msb-url",
                        "a@b.com", "--string-param", "blah", "--long-opt", "10", "--yaml-param", "yamlFile"};
        openOCliUtils.populateParams(paramslist, Arrays.asList(args));

        List<String> expectedList = Arrays.asList(args);

        Assert.assertEquals("openo-username", expectedList.get(2), paramslist.get(5).getValue());
        Assert.assertEquals("openo-password", expectedList.get(4), paramslist.get(2).getValue());
        Assert.assertEquals("msb-url", expectedList.get(6), paramslist.get(1).getValue());
        Assert.assertEquals("string-param", expectedList.get(8), paramslist.get(3).getValue());
        Assert.assertEquals("long-opt", expectedList.get(10), paramslist.get(4).getValue());
        Assert.assertEquals("yaml-param", expectedList.get(12), paramslist.get(0).getValue());

    }

    @Test
    public void testpopulateParamsShort() throws OpenOCliArgumentValueMissing, OpenOCliInvalidArgument  {

        OpenOCommandParameter param1 = new OpenOCommandParameter();
        param1.setShortOption("u");
        param1.setName("openo-username");
        OpenOCommandParameter param2 = new OpenOCommandParameter();
        param2.setShortOption("p");
        param2.setName("openo-password");
        OpenOCommandParameter param3 = new OpenOCommandParameter();
        param3.setShortOption("r");
        param3.setName("msb-url");
        OpenOCommandParameter param4 = new OpenOCommandParameter();
        param4.setShortOption("c");
        param4.setName("string-param");
        OpenOCommandParameter param5 = new OpenOCommandParameter();
        param5.setShortOption("l");
        param5.setName("long-opt");
        OpenOCommandParameter param6 = new OpenOCommandParameter();
        param6.setShortOption("y");
        param6.setName("yaml-param");

        List<OpenOCommandParameter> paramslist = new ArrayList<>();
        paramslist.add(param1);
        paramslist.add(param2);
        paramslist.add(param3);
        paramslist.add(param4);
        paramslist.add(param5);
        paramslist.add(param6);

        String[] args11 = new String[] {"bool", "-u", "admin", "-p", "123", "-r", "a@b.com", "-c", "blah", "-l", "10",
                        "-y", "yamlFile"};
        openOCliUtils.populateParams(paramslist, Arrays.asList(args11));

        List<String> expectedList = Arrays.asList(args11);

        Assert.assertEquals("u", expectedList.get(2), paramslist.get(5).getValue());
        Assert.assertEquals("-p", expectedList.get(4), paramslist.get(2).getValue());
        Assert.assertEquals("r", expectedList.get(6), paramslist.get(1).getValue());
        Assert.assertEquals("c", expectedList.get(8), paramslist.get(3).getValue());
        Assert.assertEquals("l", expectedList.get(10), paramslist.get(4).getValue());
        Assert.assertEquals("y", expectedList.get(12), paramslist.get(0).getValue());

    }
}
