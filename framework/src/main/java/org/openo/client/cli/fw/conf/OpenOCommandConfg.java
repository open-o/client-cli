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
public class OpenOCommandConfg {

    public static String DEFAULT_PARAMETER_FILE_NAME = "default_input_parameters.yaml";

    //Common parameters used across all commands.
    public static String DEAFULT_PARAMETER_USERNAME = "openo-username";
    public static String DEAFULT_PARAMETER_PASSWORD = "openo-password";
    public static String DEAFULT_PARAMETER_MSB_URL = "msb-url";
    public static String DEFAULT_PARAMETER_HELP = "help";
    public static String DEFAULT_PARAMETER_VERSION = "version";
    public static String DEFAULT_PARAMETER_DEBUG = "debug";
    public static String DEFAULT_PARAMETER_OUTPUT_FORMAT = "format";
    public static String DEFAULT_PARAMETER_OUTPUT_ATTR_LONG = "long";
    public static String DEFAULT_PARAMETER_OUTPUT_NO_TITLE = "no-title";

    //Config properties
    private static String CONF = "openo.properties";
    private static String OPENO_IGNORE_AUTH = "cli.ignore_auth";
    private static String OPENO_CLI_VERSION = "cli.version";
    private static String HTTP_API_KEY_USE_COOKIES = "http.api_key_use_cookies";
    private static String HTTP_X_AUTH_TOKEN = "http.x_auth_token";

    //used while printing the column name during PORTRAIT mode print
    public static String PORTRAINT_COLUMN_NAME_PROPERTY = "property";
    public static String PORTRAINT_COLUMN_NAME_VALUE = "value";

    public static String EXTERNAL_SCHEMA_DIRECTORY = "openo-cli-schema";
    public static String EXTERNAL_SCHEMA_PATH_PATERN = EXTERNAL_SCHEMA_DIRECTORY + "/**/*.yaml";
    public static String EXTERNAL_DISCOVERY_DIRECTORY = "data";
    public static String EXTERNAL_DISCOVERY_FILE = "external-schema.json";

    private static Properties prps = new Properties();

    static {
        try {
            prps.load(OpenOCommandConfg.class.getClassLoader().getResourceAsStream(CONF));
        } catch (IOException e) {
            //This exception will never occur
        }
    }

    /**
     * is auth service ignored.
     *
     * @return boolean
     */
    public static boolean isAuthIgnored() {
        if (prps.getProperty(OPENO_IGNORE_AUTH).equals("true")) {
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
        if (prps.getProperty(HTTP_API_KEY_USE_COOKIES).equals("true")) {
            return true;
        }

        return false;
    }

    public static String getXAuthTokenName() {
        return prps.getProperty(HTTP_X_AUTH_TOKEN, "X-Auth-Token");
    }

}
