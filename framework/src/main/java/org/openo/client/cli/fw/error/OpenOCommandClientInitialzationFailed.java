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

/**
 * Command not registered in MSB.
 *
 */
public class OpenOCommandClientInitialzationFailed extends OpenOCommandException {

    private static final long serialVersionUID = 8580121615330415000L;
    private static final String ERROR_CODE = "0x0021";
    private static final String ERROR_MESSAGE1 = "API client for the command ";
    private static final String ERROR_MESSAGE2 = " is failed, ";

    public OpenOCommandClientInitialzationFailed(String cmd, String error) {
        super(ERROR_CODE, ERROR_MESSAGE1 + cmd + ERROR_MESSAGE2 + error);
    }

    public OpenOCommandClientInitialzationFailed(String cmd, Throwable throwable) {
        this(cmd, throwable.getMessage());
    }
}
