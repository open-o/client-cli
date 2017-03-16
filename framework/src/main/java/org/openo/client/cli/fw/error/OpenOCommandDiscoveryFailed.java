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

package org.openo.client.cli.fw.error;

public class OpenOCommandDiscoveryFailed extends OpenOCommandException {

    private static final long serialVersionUID = 424464582747161435L;

    public OpenOCommandDiscoveryFailed(String name) {
        super("0x0010", "Failed auto discover schema files from " + name + " under class path");
    }

    public OpenOCommandDiscoveryFailed(String directory, String fileName) {
        super("0x0010",
                "Failed auto generate json file '" + fileName + "' under class path directory '" + directory + "'");
    }

    public OpenOCommandDiscoveryFailed(String name, Throwable throwable) {
        super("0x0010",
                "Failed auto discover schema files from " + name + " under class path, " + throwable.getMessage());
    }

    public OpenOCommandDiscoveryFailed(String directory, String fileName, Throwable throwable) {
        super("0x0010", "Failed auto generate json file '" + fileName + "' under class path directory '" + directory
                + "' , " + throwable.getMessage());
    }
}
