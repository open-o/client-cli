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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OpenOCommandErrorTest {

    @Test
    public void openOCommandDiscoveryFailedTest() {
        OpenOCommandDiscoveryFailed failed = new OpenOCommandDiscoveryFailed("name");
        assertEquals("0x0010::Failed auto discover schema files from name under class path, ", failed.getMessage());
        failed = new OpenOCommandDiscoveryFailed("directory", "name");
        assertEquals("0x0010::Failed auto generate json file 'name' under class path directory 'directory' , ",
                failed.getMessage());
    }

    @Test
    public void openOCommandInvalidParameterValueTest() {
        OpenOCommandInvalidParameterValue failed = new OpenOCommandInvalidParameterValue("name");
        assertEquals("0x0028::Parameter name value is invalid, ", failed.getMessage());
    }

    @Test
    public void openOCommandResultMapProcessingFailedTest() {
        OpenOCommandResultMapProcessingFailed failed = new OpenOCommandResultMapProcessingFailed("name",
                new Exception(""));
        assertEquals("0x0028::Failed to process the result map name in http section,  ", failed.getMessage());
    }

    @Test
    public void openOCommandHttpHeaderNotFoundTest() {
        OpenOCommandHttpHeaderNotFound failed = new OpenOCommandHttpHeaderNotFound("name");
        assertEquals("0x0027::Http header name is not returned from the service", failed.getMessage());
    }

    @Test
    public void openOCommandClientInitialzationFailedTest() {
        OpenOCommandClientInitialzationFailed failed = new OpenOCommandClientInitialzationFailed("Test",
                new Exception("Test Command Failed"));
        assertEquals("0x0021::API client for the command Test is failed, Test Command Failed", failed.getMessage());
    }

    @Test
    public void openOCommandExceptionTest1() {
        OpenOCommandException failed = new OpenOCommandException("1", "Test Command Failed");
        assertEquals("1::Test Command Failed", failed.getMessage());
    }

    @Test
    public void openOCommandExceptionTest2() {
        OpenOCommandException failed = new OpenOCommandException("1", "Test Command Failed", 201);
        assertEquals("201::1::Test Command Failed", failed.getMessage());
    }

    @Test
    public void openOCommandExecutionFailedTest1() {
        OpenOCommandExecutionFailed failed = new OpenOCommandExecutionFailed("Test", "Test Command Failed", 201);
        assertEquals("201::0x0001::Command Test failed to execute, Test Command Failed", failed.getMessage());
        failed = new OpenOCommandExecutionFailed("Test", new Exception("Test Command Failed"), 201);
        assertEquals("201::0x0001::Command Test failed to execute, Test Command Failed", failed.getMessage());
    }

    @Test
    public void openOCommandExecutionFailedTest2() {
        OpenOCommandExecutionFailed failed = new OpenOCommandExecutionFailed("Test Command Failed");
        assertEquals("0x0001::Test Command Failed", failed.getMessage());
    }

    @Test
    public void openOCommandExecutionFailedTest3() {
        OpenOCommandExecutionFailed failed = new OpenOCommandExecutionFailed("Test", "Test Command Failed");
        assertEquals("0x0001::Command Test failed to execute, Test Command Failed", failed.getMessage());

        failed = new OpenOCommandExecutionFailed("Test", new Exception("Test Command Failed"));
        assertEquals("0x0001::Command Test failed to execute, Test Command Failed", failed.getMessage());
    }

    @Test
    public void openOCommandExecutorInfoMissingTest() {
        OpenOCommandExecutorInfoMissing failed = new OpenOCommandExecutorInfoMissing("Test");

        assertEquals("0x0023::Command Test excutor info is missing from schema", failed.getMessage());
    }

    @Test
    public void openOCommandHelpFailedTest() {
        OpenOCommandHelpFailed failed = new OpenOCommandHelpFailed(new Exception("Failed"));

        assertEquals("0x0002::Command failed to print help message, Failed", failed.getMessage());
    }

    @Test
    public void openOCommandHttpFailureTest1() {
        OpenOCommandHttpFailure failed = new OpenOCommandHttpFailure("Failed");
        assertEquals("0x0025::Failed", failed.getMessage());

        failed = new OpenOCommandHttpFailure(new Exception("failed"), 201);
        assertEquals("201::0x0025::failed", failed.getMessage());
    }

    @Test
    public void openOCommandHttpFailureTest2() {
        OpenOCommandHttpFailure failed = new OpenOCommandHttpFailure("Failed", 203);

        assertEquals("203::0x0025::Failed", failed.getMessage());
    }

    @Test
    public void openOCommandInvalidParameterTypeTest() {
        OpenOCommandInvalidParameterType failed = new OpenOCommandInvalidParameterType("Failed");

        assertEquals("0x0003::Parameter type Failed is invalid", failed.getMessage());
    }

    @Test
    public void openOCommandInvalidPrintDirectionTest() {
        OpenOCommandInvalidPrintDirection failed = new OpenOCommandInvalidPrintDirection("Direction");

        assertEquals("0x0004::Print direction Direction is invalid", failed.getMessage());
    }

    @Test
    public void openOCommandInvalidRegistrationTest() {
        OpenOCommandInvalidRegistration failed = new OpenOCommandInvalidRegistration(OpenOCommandErrorTest.class);

        assertEquals("0x0005::Invalid commad class org.openo.client.cli.fw.error.OpenOCommandErrorTest registration, "
                + "it should be derived from org.openo.client.cli.fw.OpenOCommand", failed.getMessage());
    }

    @Test
    public void openOCommandInvalidResultAttributeScopeTest() {
        OpenOCommandInvalidResultAttributeScope failed = new OpenOCommandInvalidResultAttributeScope("Attribute");

        assertEquals("0x0006::Result atrribute Attribute is invalid", failed.getMessage());
    }

    @Test
    public void openOCommandInvalidSchemaTest() {
        OpenOCommandInvalidSchema failed = new OpenOCommandInvalidSchema("Schema", "Failed");

        assertEquals("0x0007::Command schema Schema is invalid, Failed", failed.getMessage());
    }

    @Test
    public void openOCommandInvalidSchemaVersionTest() {
        OpenOCommandInvalidSchemaVersion failed = new OpenOCommandInvalidSchemaVersion("1.0");

        assertEquals("0x0008::Command schema openo_cmd_schema_version 1.0 is invalid or missing", failed.getMessage());
    }

    @Test
    public void openOCommandLoginFailedTest1() {
        OpenOCommandLoginFailed failed = new OpenOCommandLoginFailed(new Exception("Failed"));

        assertEquals("0x0009::Login failed, Failed", failed.getMessage());
    }

    @Test
    public void openOCommandLoginFailedTest2() {
        OpenOCommandLoginFailed failed = new OpenOCommandLoginFailed("Failed", 201);

        assertEquals("201::0x0009::Login failed, Failed", failed.getMessage());
    }

    @Test
    public void openOCommandLogoutFailedTest() {
        OpenOCommandLogoutFailed failed = new OpenOCommandLogoutFailed(new Exception("Failed"));
        assertEquals("0x0010::Logout failed, Failed", failed.getMessage());

        failed = new OpenOCommandLogoutFailed(200);
        assertEquals("200::0x0010::Logout failed, ", failed.getMessage());
    }

    @Test
    public void openOCommandNotFoundTest() {
        OpenOCommandNotFound failed = new OpenOCommandNotFound("Test");

        assertEquals("0x0011::Command Test is not registered", failed.getMessage());
    }

    @Test
    public void openOCommandNotInitializedTest() {
        OpenOCommandNotInitialized failed = new OpenOCommandNotInitialized("Test");

        assertEquals("0x0012::Command Test is not initialized", failed.getMessage());
    }

    @Test
    public void openOCommandOutputPrintingFailedTest() {
        OpenOCommandOutputPrintingFailed failed = new OpenOCommandOutputPrintingFailed(new Exception("error"));

        assertEquals("0x0014::Command is failed to print the result, error", failed.getMessage());
    }

    @Test
    public void openOCommandParameterMissingTest() {
        OpenOCommandParameterMissing failed = new OpenOCommandParameterMissing("paramName");

        assertEquals("0x0015::Parameter paramName is mandatory", failed.getMessage());
    }

    @Test
    public void openOCommandParameterNameConflictTest() {
        OpenOCommandParameterNameConflict failed = new OpenOCommandParameterNameConflict("paramName");

        assertEquals("0x0016::Parameter name paramName is in conflict", failed.getMessage());
    }

    @Test
    public void openOCommandParameterOptionConflictTest() {
        OpenOCommandParameterOptionConflict failed = new OpenOCommandParameterOptionConflict("option");

        assertEquals("0x0017::Parameter option option is in conflict, only one option is allowed with given name",
                failed.getMessage());
    }

    @Test
    public void openOCommandRegistrationFailedTest() {
        OpenOCommandRegistrationFailed failed = new OpenOCommandRegistrationFailed("Test", "error");

        assertEquals("0x0018::Command Test is failed to register, error", failed.getMessage());
    }

    @Test
    public void openOCommandResultInitialzationFailedTest() {
        OpenOCommandResultInitialzationFailed failed = new OpenOCommandResultInitialzationFailed("Test",
                new Exception("error"));

        assertEquals("0x0022::Command Test result format is failed, error", failed.getMessage());
    }

    @Test
    public void openOCommandSchemaNotFoundTest() {
        OpenOCommandSchemaNotFound failed = new OpenOCommandSchemaNotFound("Test");

        assertEquals("0x0019::Command schema Test is not found, ", failed.getMessage());
    }

    @Test
    public void openOCommandServiceNotFoundTest() {
        OpenOCommandServiceNotFound failed = new OpenOCommandServiceNotFound("Service");

        assertEquals("0x0020::Service Service is not found in MSB", failed.getMessage());
    }

    @Test
    public void openOCommandOutputFormatNotsupportedTest() {
        OpenOCommandOutputFormatNotsupported failed = new OpenOCommandOutputFormatNotsupported("Format");

        assertEquals("0x0013::Command  does not support the output format Format", failed.getMessage());
    }

}
