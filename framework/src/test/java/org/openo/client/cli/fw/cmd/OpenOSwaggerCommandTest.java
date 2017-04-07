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
import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.error.OpenOCommandClientInitialzationFailed;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandResultInitialzationFailed;
import org.openo.client.cli.fw.input.OpenOCommandParameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpenOSwaggerCommandTest {

    @Test(expected = OpenOCommandResultInitialzationFailed.class)
    public void initializeResultTest1() throws OpenOCommandException {
        OpenOSwaggerCommandImpl swagger = new OpenOSwaggerCommandImpl();
        swagger.initializeSchema("openo-test-schema.yaml");
        List<String> obj = new ArrayList<>();
        obj.add("name");
        obj.add("get");
        swagger.initializeResult(obj);

        Set<String> obj1 = new HashSet<>();
        obj.add("name");
        obj.add("get");
        swagger.initializeResult(obj1);
    }

    @Test(expected = OpenOCommandResultInitialzationFailed.class)
    public void initializeResultTest2() throws OpenOCommandException {
        OpenOSwaggerCommandImpl swagger = new OpenOSwaggerCommandImpl();
        swagger.initializeSchema("openo-test-schema.yaml");
        Set<String> obj1 = new HashSet<>();
        obj1.add("name");
        obj1.add("get");
        swagger.initializeResult(obj1);
    }

    @Test(expected = NullPointerException.class)
    public void initializeResultTest3() throws OpenOCommandException {
        OpenOSwaggerCommandImpl swagger = new OpenOSwaggerCommandImpl();
        swagger.initializeSchema("openo-test-schema.yaml");
        ApiClient cit = new ApiClient();
        cit.setApiKey("apiKey");
        cit.setBasePath("basePath");
        swagger.initializeApiClient(cit);
    }

    class OpenOSwaggerCommandImpl extends OpenOSwaggerCommand {
        protected void run() throws OpenOCommandException {
        }

    }

    class ApiClient {

        private String basePath;
        private String apiKey;

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

    }
}
