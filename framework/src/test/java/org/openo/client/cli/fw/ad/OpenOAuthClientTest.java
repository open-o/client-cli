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
import org.openo.client.cli.fw.error.OpenOCommandExecutionFailed;
import org.openo.client.cli.fw.error.OpenOCommandHttpFailure;
import org.openo.client.cli.fw.error.OpenOCommandLoginFailed;
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
    public void loginFailedAuthIgnoredTest() throws OpenOCommandHttpFailure, OpenOCommandLoginFailed,
            OpenOCommandExecutionFailed, OpenOCommandServiceNotFound {
        OpenOCredentials creds = new OpenOCredentials("test", "test123", "http://192.168.99.10:80");
        OpenOAuthClient client = new OpenOAuthClient(creds, true);
        if (OpenOCommandConfg.isAuthIgnored()) {
            client.login();
            assertEquals(null, client.getAuthToken());
        }
    }

    @Test
    public void loginFailedServiceNotFoundTest()
            throws OpenOCommandHttpFailure, OpenOCommandLoginFailed, OpenOCommandExecutionFailed {
        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setStatus(404);
        mockHttpGetPost(result,null);
        try {
            client.login();
        } catch (OpenOCommandServiceNotFound e) {
            assertEquals("0x0020::Service auth v1 is not found in MSB", e.getMessage());
        }

    }

    @Test
    public void loginFailedCommandExecutionFailedTest()
            throws OpenOCommandHttpFailure, OpenOCommandLoginFailed, OpenOCommandServiceNotFound {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setStatus(401);
        mockHttpGetPost(result,null);
        try {
            client.login();
        } catch (OpenOCommandExecutionFailed e) {
            assertEquals("0x0001::Failed to retrive service auth v1", e.getMessage());
        }

    }

    @Test
    public void loginFailedWrongJasonBodyTest()
            throws OpenOCommandHttpFailure, OpenOCommandLoginFailed, OpenOCommandServiceNotFound {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setStatus(200);
        mockHttpGetPost(result,null);
        try {
            client.login();
        } catch (OpenOCommandExecutionFailed e) {
            assertEquals("0x0001::Failed to retrive service urlauth v1", e.getMessage());
        }

    }

    @Test
    @Ignore
    public void loginFailedTest()
            throws OpenOCommandExecutionFailed, OpenOCommandServiceNotFound, OpenOCommandHttpFailure {

        mockIsAuthIgnored();
        HttpResult result = new HttpResult();
        result.setBody("{\"url\":\"http://192.168.4.47\"}");
        result.setStatus(200);
        mockHttpGetPost(result,null);
        mockHttpPostException(new OpenOCommandHttpFailure("Post Request Failed"));
        try {
            client.login();
        } catch (OpenOCommandLoginFailed e) {
            assertEquals("0x0009::Login failed, 0x0025::Post Request Failed", e.getMessage());
        }

        HttpResult postResult = new HttpResult();
        postResult.setStatus(401);
        mockHttpGetPost(result,postResult);

        try {
            client.login();
        } catch (OpenOCommandLoginFailed e) {
            assertEquals("401::0x0009::Login failed, null", e.getMessage());
        }

    }

    @Test
    @Ignore
    public void loginSuccessTest() throws OpenOCommandExecutionFailed, OpenOCommandServiceNotFound,
            OpenOCommandHttpFailure, OpenOCommandLoginFailed {
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

        mockHttpGetPost(result,postResult);


        client.login();
    }

    private void mockIsAuthIgnored() {

        new MockUp<OpenOCommandConfg>() {
            boolean isMock = true;

            @Mock
            public boolean isAuthIgnored(Invocation inv) {
                System.out.println("isAuthIgnored : " + isMock);
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
                System.out.println("mockHttpGet : " + isMock);
                if (isMock) {
                    isMock = false;
                    return resultGet;
                } else {
                    return inv.proceed(input);
                }
            }

            @Mock
            public HttpResult post(Invocation inv, final HttpInput input) throws OpenOCommandHttpFailure {
                System.out.println("mockHttpPost : " + isMock);
                if (isMock) {
                    isMock = false;
                    return resultPost;
                } else {
                    return inv.proceed(input);
                }
            }
        };
    }

    private void mockHttpPostException(Exception exp) {
        new MockUp<OpenOHttpConnection>() {
            boolean isMock = true;

            @Mock
            public HttpResult post(Invocation inv, final HttpInput input) throws Exception {
                System.out.println("mockHttpPostException : " + isMock);
                if (isMock) {
                    isMock = false;
                    throw exp;
                } else {
                    return inv.proceed(input);
                }
            }
        };
    }
}
