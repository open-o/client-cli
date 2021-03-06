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

package org.openo.client.cli.fw.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandHttpFailure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenOHttpConnectionTest {
    HttpInput inp = null;
    OpenOHttpConnection con = null;

    @Before
    public void setup() {
        mockHttpRequest(null);
        inp = new HttpInput();
        inp.setMethod("get");
        inp.setBody("body");
        Map<String, String> map1 = new HashMap<>();
        map1.put("header1", "value1");
        inp.setReqHeaders(map1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("query1", "value1");
        inp.setReqQueries(map2);
        Map<String, String> map = new HashMap<>();
        map.put("cookie1", "value1");
        inp.setReqCookies(map);
        inp.setUri("http://192.168.99.10:80");
    }

    @Test(expected = OpenOCommandHttpFailure.class)
    public void httpUnSecuredGetExceptionTest() throws OpenOCommandHttpFailure {
        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };
        inp.setMethod("get");
        con = new OpenOHttpConnection(false, true);
        con.getDebugInfo();
        con.setAuthToken("fdsfdsfs");
        assertTrue("fdsfdsfs".equals(con.getAuthToken()));
        con.get(inp);

    }

    @Test(expected = OpenOCommandHttpFailure.class)
    public void httpUnSecuredPostExceptionTest() throws OpenOCommandHttpFailure {
        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };

        inp.setMethod("post");
        con = new OpenOHttpConnection(false, true);
        con.post(inp);
    }


    @Test(expected = OpenOCommandHttpFailure.class)
    public void httpUnSecuredPostExceptionTest1() throws OpenOCommandHttpFailure {
        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };

        inp.setMethod("post");
        inp.setBinaryData(true);
        con = new OpenOHttpConnection(false, true);
        con.post(inp);
    }

    @Test(expected = OpenOCommandHttpFailure.class)
    public void httpUnSecuredPutExceptionTest() throws OpenOCommandHttpFailure {
        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };
        inp.setMethod("put");
        con = new OpenOHttpConnection(false, true);
        con.put(inp);
    }

    @Test(expected = OpenOCommandHttpFailure.class)
    public void httpUnSecuredDeleteExceptionTest() throws OpenOCommandHttpFailure {
        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };
        inp.setMethod("delete");
        con = new OpenOHttpConnection(false, true);
        con.delete(inp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void httpUnSecuredOtherExceptionTest() throws OpenOCommandHttpFailure {
        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };
        inp.setMethod("other");
        con = new OpenOHttpConnection(false, true);
        con.request(inp);
    }

    @Test()
    public void httpUnSecuredCloseExceptionTest() throws OpenOCommandHttpFailure {
        inp.setMethod("other");
        con = new OpenOHttpConnection(false, true);
        con.close();
    }

    @Test
    public void httpSecuredGetExceptionTest() {

        // ProtocolVersion p = new ProtocolVersion("http",1,0);
        // HttpResponse hr = DefaultHttpResponseFactory.INSTANCE.newHttpResponse(p, 200 , null) ;

        new MockUp<CloseableHttpClient>() {
            @Mock
            public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context)
                    throws IOException, ClientProtocolException {

                throw new IOException("IO Exception");
            }
        };
        try {
            HttpInput inp = new HttpInput();
            inp.setMethod("get");
            inp.setBody("body");
            inp.setReqHeaders(new HashMap<String, String>());
            inp.setReqQueries(new HashMap<String, String>());
            inp.setUri("http://192.168.99.10:80");
            OpenOHttpConnection con = new OpenOHttpConnection(true, false);
            con.get(inp);
        } catch (OpenOCommandHttpFailure e) {
            assertEquals("0x0025::IO Exception", e.getMessage());
        }
    }

    private static void mockHttpRequest(HttpResult result) {
        new MockUp<OpenOHttpConnection>() {
            boolean isMock = false;

            @Mock
            public HttpResult request(Invocation inv, HttpInput input) throws OpenOCommandHttpFailure {
                if (isMock) {
                    return result;
                } else {
                    return inv.proceed(input);
                }
            }
        };
    }
}
