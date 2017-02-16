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

import mockit.Mock;
import mockit.MockUp;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandHttpFailure;

import java.io.IOException;
import java.util.HashMap;

public class OpenOHttpConnectionTest {

    @Test
    public void httpUnSecuredGetExceptionTest() {

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
            OpenOHttpConnection con = new OpenOHttpConnection(false, true);
            con.get(inp);
        } catch (OpenOCommandHttpFailure e) {
            assertEquals("0x0025::IO Exception", e.getMessage());
        }
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
}
