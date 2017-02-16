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
 * Command execution failed.
 *
 */
public class OpenOCommandHttpFailure extends OpenOCommandException {
    private static final long serialVersionUID = 488775545436993345L;

    private static String errorCode = "0x0025";

    public OpenOCommandHttpFailure(String  error, long httpStatus) {
        super(errorCode, error, httpStatus);
    }

    public OpenOCommandHttpFailure(String  error) {
        super(errorCode, error);
    }
}
