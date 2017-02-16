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

import java.util.HashMap;
import java.util.Map;

/**
 * Captures HTTP request URI, body and request &query parameters. <br>
 */
public class HttpInput {
    public static final String URI = "uri";
    public static final String BODY = "body";
    public static final String MERHOD = "method";
    public static final String HEADERS = "headers";
    public static final String QUERIES = "queries";
    public static final String COOKIES = "cookies";
    private String uri = "";

    private String body = "";

    private String method = "";

    private Map<String, String> reqHeaders = new HashMap<>();

    private Map<String, String> reqQueries = new HashMap<>();

    private Map<String, String> reqCookies = new HashMap<>();

    public String getUri() {
        return this.uri;
    }

    public HttpInput setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getBody() {
        return this.body;
    }

    public HttpInput setBody(String body) {
        this.body = body;
        return this;
    }

    public Map<String, String> getReqHeaders() {
        return this.reqHeaders;
    }

    /**
     * header parameter setter.
     *
     * @param reqHeaders
     *            header map
     * @return HttpInput
     */
    public HttpInput setReqHeaders(Map<String, String> reqHeaders) {
        if (reqHeaders != null) {
            this.reqHeaders = reqHeaders;
        }
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public HttpInput setMethod(String method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getReqQueries() {
        return reqQueries;
    }

    /**
     * Request query parameters.
     *
     * @param reqQueries
     *            request queries
     * @return HttpInput
     */
    public HttpInput setReqQueries(Map<String, String> reqQueries) {
        if (reqQueries != null) {
            this.reqQueries = reqQueries;
        }
        return this;
    }

    public Map<String, String> getReqCookies() {
        return reqCookies;
    }

    public HttpInput setReqCookies(Map<String, String> reqCookies) {
        this.reqCookies = reqCookies;
        return this;
    }

    @Override
    public String toString() {
        return "\nURL: " + this.getUri() + "\nMethod: " + this.getMethod() + "\nQueries: " + this.getReqQueries()
                + "\nRequest Body: " + this.getBody() + "\nRequest Headers: " + this.getReqHeaders().toString()
                + "\nRequest Cookies: " + this.getReqQueries().toString();
    }
}
