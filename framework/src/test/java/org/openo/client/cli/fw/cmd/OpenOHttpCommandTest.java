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
import org.openo.client.cli.fw.http.HttpInput;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.input.ParameterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenOHttpCommandTest {

    @Test(expected = OpenOCommandException.class)
    public void runTest() throws OpenOCommandException {
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

        HttpInput inp = new HttpInput();
        inp.setBody("body");
        inp.setMethod("method");
        inp.setReqCookies(new HashMap<String, String>());
        inp.setReqHeaders(new HashMap<String, String>());
        inp.setReqQueries(new HashMap<String, String>());
        inp.setUri("uri");

        OpenOHttpCommand com = new OpenOHttpCommand();
        com.setParameters(paramslist);
        com.getParameters();
        com.getParametersMap();
        com.setInput(inp);
        com.initializeSchema("sample-test-schema.yaml");
        com.execute();

    }

}
