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

package org.openo.client.cli.main.error;

import org.openo.client.cli.fw.error.OpenOCommandException;

/**
 * CLI argument missing.
 *
 */
public class OpenOCliInvalidArgument extends OpenOCommandException {

    private static final long serialVersionUID = -1438492553530993246L;
    private static final String ERROR_CODE = "0x1000";
    private static final String ERROR_MESSAGE1 = "Invalid argument ";

    public OpenOCliInvalidArgument(String arg) {
        super(ERROR_CODE, ERROR_MESSAGE1 + arg);
    }

    public OpenOCliInvalidArgument(String arg, String errorMessage) {
        super(ERROR_CODE, ERROR_MESSAGE1 + arg + " , " + errorMessage);
    }

    public OpenOCliInvalidArgument(String arg, Throwable throwable) {
        this(arg , throwable.getMessage());
    }
}
