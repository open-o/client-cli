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

package org.openo.client.cli.fw.conf;

import java.io.IOException;
import java.util.Properties;

/**
 * Open-O command constants.
 *
 */
public final class OpenOCommandConfg {

    public static final String DEFAULT_PARAMETER_FILE_NAME = "default_input_parameters.yaml";

    // Common parameters used across all commands.
    public static final String DEAFULT_PARAMETER_USERNAME = "openo-username";
    public static final String DEAFULT_PARAMETER_PASS_WORD = "openo-password";
    public static final String DEAFULT_PARAMETER_MSB_URL = "msb-url";
    public static final String DEFAULT_PARAMETER_HELP = "help";
    public static final String DEFAULT_PARAMETER_VERSION = "version";
    public static final String DEFAULT_PARAMETER_DEBUG = "debug";
    public static final String DEFAULT_PARAMETER_OUTPUT_FORMAT = "format";
    public static final String DEFAULT_PARAMETER_OUTPUT_ATTR_LONG = "long";
    public static final String DEFAULT_PARAMETER_OUTPUT_NO_TITLE = "no-title";

    // Config properties
    private static final String CONF = "openo.properties";
    private static final String OPENO_IGNORE_AUTH = "cli.ignore_auth";
    private static final String OPENO_CLI_VERSION = "cli.version";
    private static final String HTTP_API_KEY_USE_COOKIES = "http.api_key_use_cookies";
    private static final String HTTP_X_AUTH_TOKEN = "http.x_auth_token";

    // used while printing the column name during PORTRAIT mode print
    public static final String PORTRAINT_COLUMN_NAME_PROPERTY = "property";
    public static final String PORTRAINT_COLUMN_NAME_VALUE = "value";

    public static final String EXTERNAL_SCHEMA_DIRECTORY = "openo-cli-schema";
    public static final String EXTERNAL_YAML_PATTERN = "/**/*.yaml";
    public static final String EXTERNAL_JSON_PATTERN = "/**/*.json";
    public static final String EXTERNAL_SCHEMA_PATH_PATERN = EXTERNAL_SCHEMA_DIRECTORY + EXTERNAL_YAML_PATTERN;
    public static final String EXTERNAL_DISCOVERY_DIRECTORY = "data";
    public static final String EXTERNAL_DISCOVERY_FILE = "external-schema.json";
    public static final String EXTERNAL_DISCOVERY_DIRECTORY_PATTERN = EXTERNAL_DISCOVERY_DIRECTORY
            + EXTERNAL_JSON_PATTERN;
    private static Properties prps = new Properties();

    /**
     * Private constructor.
     */
    private OpenOCommandConfg() {

    }

    static {
        try {
            prps.load(OpenOCommandConfg.class.getClassLoader().getResourceAsStream(CONF));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * is auth service ignored.
     *
     * @return boolean
     */
    public static boolean isAuthIgnored() {
        if ("true".equals(prps.getProperty(OPENO_IGNORE_AUTH))) {
            return true;
        }

        return false;
    }

    public static String getVersion() {
        return prps.getProperty(OPENO_CLI_VERSION);
    }

    /**
     * checks if cookies based auth.
     *
     * @return boolean
     */
    public static boolean isCookiesBasedAuth() {
        if ("true".equals(prps.getProperty(HTTP_API_KEY_USE_COOKIES))) {
            return true;
        }

        return false;
    }

    public static String getXAuthTokenName() {
        return prps.getProperty(HTTP_X_AUTH_TOKEN, "X-Auth-Token");
    }

}
