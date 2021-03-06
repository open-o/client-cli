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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OpenOCliArgumentTest {

    @Test
    public void openOCliArgumentValueMissingTest() {
        OpenOCliArgumentValueMissing failed = new OpenOCliArgumentValueMissing("Argument value missing");
        assertEquals("0x1001::Value for argument Argument value missing is missing", failed.getMessage());
    }

    @Test
    public void openOCliInvalidArgumentTest() {
        OpenOCliInvalidArgument failed = new OpenOCliInvalidArgument("Invalid Argument");
        assertEquals("0x1000::Invalid argument Invalid Argument", failed.getMessage());
        failed = new OpenOCliInvalidArgument("Invalid Argument", new Exception(""));
        assertEquals("0x1000::Invalid argument Invalid Argument , ", failed.getMessage());
    }

}
