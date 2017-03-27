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

package org.openo.client.cli.cmd.sample;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openo.client.cli.fw.conf.OpenOCommandConfg;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.error.OpenOCommandNotInitialized;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.input.ParameterType;

public class OpenOCommandSampleTest {
    @Test
    public void sampleTestVersion() {
        OpenOCommandSample sample = new OpenOCommandSample();

        try {
            List<OpenOCommandParameter> parameters = new ArrayList();
            OpenOCommandParameter v = new OpenOCommandParameter();
            v.setName(OpenOCommandConfg.DEFAULT_PARAMETER_VERSION);
            v.setValue("true");
            parameters.add(v);
            OpenOCommandParameter h = new OpenOCommandParameter();
            h.setName(OpenOCommandConfg.DEFAULT_PARAMETER_HELP);
            h.setValue("false");
            parameters.add(h);
            sample.setParameters(parameters);
            sample.execute();
        } catch (OpenOCommandException e) {
        }
    }

    @Test
    public void sampleTestHelp() {
        OpenOCommandSample sample = new OpenOCommandSample();
        try {
            List<OpenOCommandParameter> parameters = new ArrayList();
            OpenOCommandParameter v = new OpenOCommandParameter();
            v.setName(OpenOCommandConfg.DEFAULT_PARAMETER_HELP);
            v.setValue("true");
            v.setParameterType(ParameterType.BOOL);
            parameters.add(v);
            sample.setParameters(parameters);
            sample.execute();
        } catch (OpenOCommandException e) {
        }
    }

    @Test
    public void sampleTest() {
        OpenOCommandSample sample = new OpenOCommandSample();
        try {
            List<OpenOCommandParameter> parameters = new ArrayList();
            OpenOCommandParameter v = new OpenOCommandParameter();
            v.setName(OpenOCommandConfg.DEFAULT_PARAMETER_VERSION);
            v.setValue("false");
            parameters.add(v);
            OpenOCommandParameter h = new OpenOCommandParameter();
            h.setName(OpenOCommandConfg.DEFAULT_PARAMETER_HELP);
            h.setValue("false");
            parameters.add(h);
            OpenOCommandParameter f = new OpenOCommandParameter();
            f.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_FORMAT);
            f.setValue("table");
            parameters.add(f);
            OpenOCommandParameter l = new OpenOCommandParameter();
            l.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_ATTR_LONG);
            l.setValue("true");
            parameters.add(l);
            OpenOCommandParameter t = new OpenOCommandParameter();
            t.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_NO_TITLE);
            t.setValue("true");
            parameters.add(t);
            OpenOCommandParameter a = new OpenOCommandParameter();
            a.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_NO_AUTH);
            a.setValue("true");
            parameters.add(a);
            OpenOCommandParameter d = new OpenOCommandParameter();
            d.setName(OpenOCommandConfg.DEFAULT_PARAMETER_DEBUG);
            d.setValue("true");
            parameters.add(d);
            OpenOCommandParameter m = new OpenOCommandParameter();
            m.setName(OpenOCommandConfg.DEAFULT_PARAMETER_MSB_URL);
            m.setValue("http://localhost");
            parameters.add(m);
            sample.setParameters(parameters);
            sample.execute();
        } catch (OpenOCommandException e) {
        }
    }

    @Test(expected=OpenOCommandExecutionFailed.class)
    public void sampleTestFailure() throws OpenOCommandException {
        OpenOCommandSample sample = new OpenOCommandSample();
        sample.failCase = true;

        List<OpenOCommandParameter> parameters = new ArrayList();
        OpenOCommandParameter v = new OpenOCommandParameter();
        v.setName(OpenOCommandConfg.DEFAULT_PARAMETER_VERSION);
        v.setValue("false");
        parameters.add(v);
        OpenOCommandParameter h = new OpenOCommandParameter();
        h.setName(OpenOCommandConfg.DEFAULT_PARAMETER_HELP);
        h.setValue("false");
        parameters.add(h);
        OpenOCommandParameter f = new OpenOCommandParameter();
        f.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_FORMAT);
        f.setValue("table");
        parameters.add(f);
        OpenOCommandParameter l = new OpenOCommandParameter();
        l.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_ATTR_LONG);
        l.setValue("true");
        parameters.add(l);
        OpenOCommandParameter t = new OpenOCommandParameter();
        t.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_NO_TITLE);
        t.setValue("true");
        parameters.add(t);
        OpenOCommandParameter a = new OpenOCommandParameter();
        a.setName(OpenOCommandConfg.DEFAULT_PARAMETER_OUTPUT_NO_AUTH);
        a.setValue("true");
        parameters.add(a);
        OpenOCommandParameter d = new OpenOCommandParameter();
        d.setName(OpenOCommandConfg.DEFAULT_PARAMETER_DEBUG);
        d.setValue("true");
        parameters.add(d);
        OpenOCommandParameter m = new OpenOCommandParameter();
        m.setName(OpenOCommandConfg.DEAFULT_PARAMETER_MSB_URL);
        m.setValue("http://localhost");
        parameters.add(m);
        sample.setParameters(parameters);
        sample.execute();
    }

    @Test(expected=OpenOCommandNotInitialized.class)
    public void sampleTestIsInitialized() throws OpenOCommandException {
        OpenOCommandSample sample = new OpenOCommandSample(false);
        sample.execute();
    }
}
