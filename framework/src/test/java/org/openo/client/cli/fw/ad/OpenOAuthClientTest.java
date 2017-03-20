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

package org.openo.client.cli.fw.ad;

import static org.junit.Assert.assertEquals;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openo.client.cli.fw.conf.OpenOCommandConfg;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.error.OpenOCommandHttpFailure;
import org.openo.client.cli.fw.error.OpenOCommandLoginFailed;
import org.openo.client.cli.fw.error.OpenOCommandLogoutFailed;
import org.openo.client.cli.fw.error.OpenOCommandServiceNotFound;
import org.openo.client.cli.fw.http.HttpInput;
import org.openo.client.cli.fw.http.HttpResult;
import org.openo.client.cli.fw.http.OpenOHttpConnection;

import java.util.HashMap;
import java.util.Map;

public class OpenOAuthClientTest {

    OpenOAuthClient client;

    @Before
    public void setUp() throws OpenOCommandHttpFailure {
        OpenOCredentials creds = new OpenOCredentials("test", "test123", "http://192.168.99.10:80");
        client = new OpenOAuthClient(creds, true);
    }

    @Test
    public void loginFailedAuthIgnoredTest() throws OpenOCommandException {
        OpenOCredentials creds = new OpenOCredentials("test", "test123", "http://192.168.99.10:80");
        OpenOAuthClient client = new OpenOAuthClient(creds, true);
        if (OpenOCommandConfg.isAuthIgnored()) {
            client.login();
            assertEquals(null, client.getAuthToken());
        }
    }

    @Test
    public void loginFailedServiceNotFoundTest() throws OpenOCommandException {
        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setStatus(404);
        mockHttpGetPost(result, null);
        try {
            client.login();
        } catch (OpenOCommandServiceNotFound e) {
            assertEquals("0x0020::Service auth v1 is not found in MSB", e.getMessage());
        }

    }

    @Test
    public void loginFailedCommandExecutionFailedTest() throws OpenOCommandException {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setStatus(401);
        mockHttpGetPost(result, null);
        try {
            client.login();
        } catch (OpenOCommandExecutionFailed e) {
            assertEquals("0x0001::Failed to retrive service auth v1", e.getMessage());
        }

    }

    @Test
    public void loginFailedWrongJasonBodyTest() throws OpenOCommandException {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setStatus(200);
        mockHttpGetPost(result, null);
        try {
            client.login();
        } catch (OpenOCommandExecutionFailed e) {
            assertEquals("0x0001::Failed to retrive service url, auth v1, json string can not be null or empty",
                    e.getMessage());
        }

    }

    @Test
    @Ignore
    public void loginFailedTest() throws OpenOCommandException {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);
        mockOpenOAuthClient();
        try {
            client.login();
        } catch (OpenOCommandLoginFailed e) {
            assertEquals("0x0009::Login failed, 0x0025::null", e.getMessage());
        }

    }

    @Test
    @Ignore
    public void logoutFailedTest() throws OpenOCommandException {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);
        mockOpenOAuthClient();
        try {
            client.logout();
        } catch (OpenOCommandLogoutFailed e) {
            assertEquals("0x0010::Logout failed, 0x0025::null", e.getMessage());
        }

    }

    @Test
    @Ignore
    public void loginSuccessTest() throws OpenOCommandException {
        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);

        HttpResult postResult = new HttpResult();
        postResult.setStatus(200);

        Map<String, String> respHeaders = new HashMap<>();
        respHeaders.put(OpenOHttpConnection.X_AUTH_TOKEN, "authtoken");
        postResult.setRespHeaders(respHeaders);

        Map<String, String> respCookies = new HashMap<>();
        respHeaders.put(OpenOHttpConnection.X_AUTH_TOKEN, "authtoken");
        postResult.setRespCookies(respCookies);

        mockHttpGetPost(result, postResult);

        client.login();
    }

    private void mockIsAuthIgnored() {

        new MockUp<OpenOCommandConfg>() {
            boolean isMock = true;

            @Mock
            public boolean isAuthIgnored(Invocation inv) {
                if (isMock) {
                    isMock = false;
                    return false;
                } else {
                    return inv.proceed();
                }
            }
        };
    }

    private void mockHttpGetPost(HttpResult resultGet, HttpResult resultPost) {
        new MockUp<OpenOHttpConnection>() {
            boolean isMock = true;

            @Mock
            public HttpResult get(Invocation inv, final HttpInput input) throws OpenOCommandHttpFailure {
                if (isMock) {
                    isMock = false;
                    return resultGet;
                } else {
                    return inv.proceed(input);
                }
            }

            @Mock
            public HttpResult post(Invocation inv, final HttpInput input) throws OpenOCommandHttpFailure {
                if (isMock) {
                    isMock = false;
                    return resultPost;
                } else {
                    return inv.proceed(input);
                }
            }
        };
    }

    private void mockOpenOAuthClient() {
        new MockUp<OpenOAuthClient>() {
            boolean isMock = true;

            @Mock
            public HttpResult run(Invocation inv, HttpInput input) throws OpenOCommandHttpFailure {
                if (isMock) {
                    isMock = false;
                    return new HttpResult();
                } else {
                    return inv.proceed(input);
                }
            }

            @Mock
            private String getAuthUrl(Invocation inv) throws OpenOCommandException {
                if (isMock) {
                    isMock = false;
                    return "uri";
                } else {
                    return inv.proceed();
                }
            }
        };
    }
}
