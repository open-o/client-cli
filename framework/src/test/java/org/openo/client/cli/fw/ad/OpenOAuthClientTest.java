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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openo.client.cli.fw.conf.OpenOCommandConfg;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.error.OpenOCommandHttpFailure;
import org.openo.client.cli.fw.error.OpenOCommandServiceNotFound;
import org.openo.client.cli.fw.http.HttpInput;
import org.openo.client.cli.fw.http.HttpResult;
import org.openo.client.cli.fw.http.OpenOHttpConnection;

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
            client.getDebugInfo();
            client.login();
            assertEquals(null, client.getAuthToken());
        }
    }

    @Test
    public void logoutFailedAuthIgnoredTest() throws OpenOCommandException {
        OpenOCredentials creds = new OpenOCredentials("test", "test123", "http://192.168.99.10:80");
        OpenOAuthClient client = new OpenOAuthClient(creds, true);
        if (OpenOCommandConfg.isAuthIgnored()) {
            client.logout();
            assertEquals(null, client.getAuthToken());
        }
    }

    @Test
    public void getMsbUrlTest() throws OpenOCommandException {
        OpenOCredentials creds = new OpenOCredentials("test", "test123", "http://192.168.99.10:80");
        OpenOAuthClient client = new OpenOAuthClient(creds, true);
        OpenOService srv = new OpenOService();
        srv.setName("msb");
        String msb = client.getServiceBasePath(srv);
        assertEquals("http://192.168.99.10:80/api/microservices/v1", msb);
    }

    @Test(expected = OpenOCommandServiceNotFound.class)
    public void loginFailedServiceNotFoundTest() throws OpenOCommandException {
        mockIsAuthIgnored(false);
        HttpResult result = new HttpResult();
        result.setStatus(404);
        mockHttpRequest(result);
        client.login();

    }

    @Test(expected = OpenOCommandExecutionFailed.class)
    public void loginFailedCommandExecutionFailedTest() throws OpenOCommandException {

        mockIsAuthIgnored(false);
        HttpResult result = new HttpResult();
        result.setStatus(401);
        mockHttpRequest(result);
        client.login();
    }

    @Test(expected = OpenOCommandExecutionFailed.class)
    public void loginFailedWrongJasonBodyTest() throws OpenOCommandException {
        mockIsAuthIgnored(false);
        HttpResult result = new HttpResult();
        result.setStatus(200);
        mockHttpRequest(result);
        client.login();
    }

    @Test
    public void loginSuccessTest() {

        mockIsAuthIgnored(false);
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);
        mockHttpConsecutiveRequest(result);
        try {
            client.login();
        } catch (OpenOCommandException e) {
        }
        mockHttpRequest(null);
    }

    @Test
    public void logoutFailedTest() {

        mockIsAuthIgnored(false);
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);
        mockHttpConsecutiveRequest(result);
        try {
            client.logout();
        } catch (OpenOCommandException e) {
        }
        mockHttpRequest(null);
    }

    @Test
    public void logoutSuccessTest() {

        mockIsAuthIgnored(false);
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(204);
        mockHttpConsecutiveRequest(result);
        try {
            client.logout();
        } catch (OpenOCommandException e) {
        }
        mockHttpRequest(null);
    }

    private void mockIsAuthIgnored(boolean isAuthIgnored) {

        new MockUp<OpenOCommandConfg>() {
            boolean isMock = true;

            @Mock
            public boolean isAuthIgnored(Invocation inv) {
                if (isMock) {
                    isMock = false;
                    return isAuthIgnored;
                } else {
                    return inv.proceed();
                }
            }
        };
    }

    private static void mockHttpRequest(HttpResult result) {
        new MockUp<OpenOHttpConnection>() {
            boolean isMock = true;

            @Mock
            public HttpResult request(Invocation inv, HttpInput input) throws OpenOCommandHttpFailure {
                if (isMock) {
                    isMock = false;
                    return result;
                } else {
                    return inv.proceed(input);
                }
            }
        };
    }

    private void mockHttpConsecutiveRequest(HttpResult result) {
        new MockUp<OpenOHttpConnection>() {
            @Mock
            public HttpResult request(Invocation inv, HttpInput input) throws OpenOCommandHttpFailure {
                return result;
            }
        };
    }

    @AfterClass
    public static void clear() {
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);
        mockHttpRequest(result);
    }
}
