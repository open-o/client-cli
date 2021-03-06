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

/**
 * Constants.
 *
 */
public class Constants {

    public static final String SSLCONTEST_TLS = "TLSV1.2";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_AUTH_TOKEN = "X-Auth-Token";

    public static final String AUTH_SERVICE = "auth";
    public static final String AUTH_SERVICE_VERSION = "v1";
    public static final String TOKEN = "{\"userName\": \"%s\",\"password\": \"%s\"}";
    public static final String MSB_URI = "/api/microservices/v1";
    public static final String MSB_SERVICE_URI = MSB_URI + "/services/%s/version/%s";

    public static final String MSB = "msb";

    //http
    public static final String URI = "uri";
    public static final String BODY = "body";
    public static final String MERHOD = "method";
    public static final String HEADERS = "headers";
    public static final String QUERIES = "queries";
    public static final String COOKIES = "cookies";

    public static final String HTTP = "http";
    public static final String REQUEST = "request";
    public static final String SAMPLE_RESPONSE = "sample_response";
    public static final String SUCCESS_CODES = "success_codes";
    public static final String RESULT_MAP = "result_map";

    //swagger
    public static final String EXECUTOR = "exec";

    public static final String API = "api";
    public static final String CLIENT = "client";
    public static final String ENTITY = "entity";
    public static final String METHOD = "method";
    public static final String EXCEPTION = "exception";

    public static final String SCOPE = "scope";

    public static final String OPENO_CMD_SCHEMA_VERSION_VALUE = "1.0";
    public static final String DESCRIPTION = "description";
    public static final String SERVICE = "service";
    public static final String PARAMETERS = "parameters";
    public static final String RESULTS = "results";

    public static final String OPENO_CMD_SCHEMA_VERSION = "openo_cmd_schema_version";
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String BASE_PATH = "base_path";
    public static final String NO_AUTH = "no-auth";

    public static final String SHORT_OPTION = "short_option";
    public static final String LONG_OPTION = "long_option";
    public static final String TYPE = "type";
    public static final String IS_OPTIONAL = "is_optional";
    public static final String DEFAULT_VALUE = "default_value";
    public static final String IS_SECURED = "is_secured";

    public static final String DIRECTION = "direction";
    public static final String ATTRIBUTES = "attributes";

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
    public static final String DEFAULT_PARAMETER_OUTPUT_NO_AUTH = "no-auth";

    // Configuration properties
    public static final String CONF = "openo.properties";
    public static final String OPENO_IGNORE_AUTH = "cli.ignore_auth";
    public static final String OPENO_CLI_VERSION = "cli.version";
    public static final String HTTP_API_KEY_USE_COOKIES = "http.api_key_use_cookies";
    public static final String HTTP_X_AUTH_TOKEN = "http.x_auth_token";

    // Used while printing the column name during PORTRAIT mode print
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

    public static final String PARAMETER_TYPE_JSON = "json";
    public static final String PARAMETER_TYPE_YAML = "yaml";
    public static final String PARAMETER_TYPE_STRING = "string";
    public static final String PARAMETER_TYPE_LONG = "long";
    public static final String PARAMETER_TYPE_URL = "url";
    public static final String PARAMETER_TYPE_BOOL = "bool";
    public static final String PARAMETER_TYPE_ARRAY = "array";
    public static final String PARAMETER_TYPE_BINARY = "binary";
    public static final String PARAMETER_TYPE_MAP = "map";

    public static final String BOOLEAN_TRUE = "true";
    public static final String BOOLEAN_FALSE = "false";

    public static final String DIRECTION_PORTRAIT = "portrait";
    public static final String DIRECTION_LANDSCAPE = "landscape";

    public static final String RESULT_SCOPE_SHORT = "short";
    public static final String RESULT_SCOPE_LONG = "long";

    public static final String POST = "post";
    public static final String GET = "get";
    public static final String DELETE = "delete";
    public static final String PUT = "put";
    public static final String HEAD = "delete";

    public static final String DEFAULT_SCHEMA_FILE_NAME = "default_input_parameters.yaml";

    // Error message
    public static final String SCHEMA_FILE_EMPTY = "The schema file cann't be null or empty";
    public static final String SCHEMA_FILE_WRONG_EXTN = "Schema file should be '.yaml' extension";
    public static final String SCHEMA_FILE_NOT_EXIST = "Schema file doesn't exist";
    public static final String HTTP_SECTION_EMPTY = "Http Section cann't be null or empty";
    public static final String HTTP_BODY_SECTION_EMPTY = "http body section under 'request:' cann't be null or empty";
    public static final String HTTP_BODY_FAILED_PARSING = "The http body json is failed to parse";
    public static final String HTTP_BODY_JSON_EMPTY = "The http body json cann't be null or empty";
    public static final String HTTP_SUCCESS_CODE_INVALID = "Invalid http success code.";
    public static final String HTTP_SAMPLE_RESPONSE_EMPTY = "Sample response cann't be null or empty";
    public static final String HTTP_SAMPLE_RESPONSE_FAILED_PARSING = "The http Sample response json is failed to parse.";

    private Constants() {
    }

}
