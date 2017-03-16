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

import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpStatus;
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

/**
 * OpenO Auth client helps to do login and logout.
 *
 */
public class OpenOAuthClient {

    public static final String AUTH_SERVICE = "auth";

    public static final String AUTH_SERVICE_VERSION = "v1";

    public static final String TOKEN = "{\"userName\": \"%s\",\"password\": \"%s\"}";

    private static final String MSB_URI = "/api/microservices/v1";

    private static final String MSB_SERVICE_URI = MSB_URI + "/services/%s/version/%s";

    private static final String MSB = "msb";

    /*
     * Open-O credentials
     */
    private OpenOHttpConnection http = null;

    private OpenOCredentials creds = null;

    public OpenOAuthClient(OpenOCredentials creds, boolean debug) throws OpenOCommandHttpFailure {
        this.creds = creds;
        this.http = new OpenOHttpConnection(creds.getMsbUrl().startsWith("https"), debug);
    }

    /**
     * Login.
     *
     * @throws OpenOCommandLoginFailed
     *             LoginFailed Exception
     * @throws OpenOCommandHttpFailure
     *             Http request failed
     * @throws OpenOCommandExecutionFailed
     *             cmd exec failed
     * @throws OpenOCommandServiceNotFound
     *             service not found
     */
    public void login() throws OpenOCommandException {

        // For development purpose, its introduced and is not supported for production
        if (OpenOCommandConfg.isAuthIgnored()) {
            return;
        }

        HttpInput input = new HttpInput().setUri(this.getAuthUrl() + "/tokens")
                .setBody(String.format(TOKEN, creds.getUsername(), creds.getPassword())).setMethod("post");

        HttpResult result;
        try {
            result = this.run(input);
        } catch (OpenOCommandHttpFailure e) {
            throw new OpenOCommandLoginFailed(e);
        }
        if (result.getStatus() != HttpStatus.SC_OK && result.getStatus() != HttpStatus.SC_CREATED) {
            throw new OpenOCommandLoginFailed(result.getBody(), result.getStatus());
        }

        if (OpenOCommandConfg.isCookiesBasedAuth()) {
            this.http.setAuthToken(result.getRespCookies().get(OpenOHttpConnection.X_AUTH_TOKEN));
        } else {
            this.http.setAuthToken(result.getRespHeaders().get(OpenOHttpConnection.X_AUTH_TOKEN));
        }
    }

    /**
     * Logout.
     *
     * @throws OpenOCommandExecutionFailed
     *             cmd exec failed
     * @throws OpenOCommandServiceNotFound
     *             service not found
     * @throws OpenOCommandLogoutFailed
     *             logout failed
     * @throws OpenOCommandHttpFailure
     *             exception
     */
    public void logout() throws OpenOCommandException {
        // For development purpose, its introduced and is not supported for production
        if (OpenOCommandConfg.isAuthIgnored()) {
            return;
        }

        HttpInput input = new HttpInput().setUri(this.getAuthUrl() + "/tokens").setMethod("delete");

        HttpResult result;
        try {
            result = this.run(input);
        } catch (OpenOCommandHttpFailure e) {
            throw new OpenOCommandLogoutFailed(e);
        }
        if (result.getStatus() != HttpStatus.SC_NO_CONTENT) {
            throw new OpenOCommandLogoutFailed(result.getStatus());
        }

        this.http.close();
    }

    /**
     * Find given service base path.
     *
     * @param srv
     *            openo service
     * @return string
     * @throws OpenOCommandExecutionFailed
     *             Cmd execution failed exception
     * @throws OpenOCommandServiceNotFound
     *             Service not found
     * @throws OpenOCommandHttpFailure
     *             http request failed
     */
    public String getServiceBasePath(OpenOService srv) throws OpenOCommandException {
        if (srv.getName().equals(MSB)) {
            return this.getMsbUrl();
        }

        HttpInput input = new HttpInput()
                .setUri(this.creds.getMsbUrl() + String.format(MSB_SERVICE_URI, srv.getName(), srv.getVersion()));
        HttpResult result = this.http.get(input);

        if (result.getStatus() == HttpStatus.SC_NOT_FOUND) {
            throw new OpenOCommandServiceNotFound(srv.toString());
        }
        if (!result.isSuccess()) {
            throw new OpenOCommandExecutionFailed("Failed to retrive service " + srv.toString());
        }

        try {
            return this.creds.getMsbUrl() + JsonPath.read(result.getBody(), "url");
        } catch (Exception e) {
            throw new OpenOCommandExecutionFailed(e, srv.toString());
        }
    }

    private String getAuthUrl() throws OpenOCommandException {
        OpenOService srv = new OpenOService();
        srv.setName(AUTH_SERVICE);
        srv.setVersion(AUTH_SERVICE_VERSION);
        return this.getServiceBasePath(srv);
    }

    private String getMsbUrl() {
        return this.creds.getMsbUrl() + MSB_URI;
    }

    public String getAuthToken() {
        return this.http.getAuthToken();
    }

    public String getDebugInfo() {
        return this.http.getDebugInfo();
    }

    /**
     * Http call to auth service.
     *
     * @param input
     *            http input
     * @return http result
     * @throws OpenOCommandHttpFailure
     *             exception
     */
    public HttpResult run(HttpInput input) throws OpenOCommandHttpFailure {
        if (OpenOCommandConfg.isCookiesBasedAuth()) {
            input.getReqCookies().put(OpenOHttpConnection.X_AUTH_TOKEN, http.getAuthToken());
        }
        return this.http.request(input);
    }
}
