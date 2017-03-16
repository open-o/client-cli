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

package org.openo.client.cli.fw.input;

import java.util.List;
import java.util.Map;

import org.openo.client.cli.fw.error.OpenOCommandInvalidParameterValue;
import org.openo.client.cli.fw.error.OpenOCommandParameterMissing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Open-O Command's input parameter.
 *
 */
public class OpenOCommandParameter {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SHORT_OPTION = "short_option";
    public static final String LONG_OPTION = "long_option";
    public static final String TYPE = "type";
    public static final String IS_OPTIONAL = "is_optional";
    public static final String DEFAULT_VALUE = "default_value";
    public static final String IS_SECURED = "is_secured";

    /*
     * Name, for positional parameters, the place is decided from schema file definition
     */
    private String cmdName;

    /*
     * Description
     */
    private String cmdDescription = "";

    /*
     * Short Option, like -f, for positional parameters, its not required.
     */
    private String shortOption = null;

    /*
     * Long Option, like --file-path, for positional parameters, its not required.
     */
    private String longOption = null;

    /*
     * Parameter type such as int, json, yaml, string, etc
     */
    private ParameterType parameterType;

    /*
     * Default value
     */
    private String defaultValue = "";

    /*
     * Is optional
     */
    private boolean isOptional = false;

    /*
     * Is secured
     */
    private boolean isSecured = false;

    /*
     * Parameter Value
     */
    private Object value;

    public String getName() {
        return cmdName;
    }

    public void setName(String name) {
        this.cmdName = name;
    }

    public String getDescription() {
        return cmdDescription;
    }

    public void setDescription(String description) {
        this.cmdDescription = description;
    }

    public String getShortOption() {
        return shortOption;
    }

    public void setShortOption(String shortOption) {
        this.shortOption = shortOption;
    }

    public String getLongOption() {
        return longOption;
    }

    public void setLongOption(String longOption) {
        this.longOption = longOption;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    /**
     * Returns default value.
     *
     * @return string
     */
    public String getDefaultValue() {
        if (this.isDefaultValueAnEnv()) {
            String envVar = this.getEnvVarNameFromDefaultValue();
            this.defaultValue = System.getenv(envVar);
        } else if (this.getParameterType().equals(ParameterType.BOOL)) {
            // For bool type always the default param is false
            this.defaultValue = "false";
        }

        return defaultValue;
    }

    /**
     * check if the default value is ${ENV_VAR_NAME}.
     *
     * @return boolean
     */
    public boolean isDefaultValueAnEnv() {
        return this.defaultValue.trim().startsWith("${") && this.defaultValue.trim().endsWith("}");
    }

    /**
     * check if the default value is ${ENV_VAR_NAME} and return the ENV_VAR_NAME.
     *
     * @return ENV_VAR_NAME
     */
    public String getEnvVarNameFromDefaultValue() {
        return this.defaultValue.trim().substring(2, this.defaultValue.length() - 1);
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns param value.
     *
     * @return value
     * @throws OpenOCommandInvalidParameterValue exception
     */
    public Object getValue() throws OpenOCommandInvalidParameterValue {
        if (value != null) {
            if (ParameterType.URL.equals(parameterType) && !value.toString().startsWith("http")
                    && !value.toString().startsWith("/")) {
                value = "/" + value;
            } else if (ParameterType.ARRAY.equals(parameterType)) {
                if (!(value instanceof List)) {
                    throw new OpenOCommandInvalidParameterValue(this.getName());
                }

                List<String> list = (List<String>) value;
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.writeValueAsString(list);
                } catch (JsonProcessingException e) {
                    throw new OpenOCommandInvalidParameterValue(this.getName());
                }
            } else if (ParameterType.MAP.equals(parameterType)) {
                if (!(value instanceof Map)) {
                    throw new OpenOCommandInvalidParameterValue(this.getName());
                }

                Map<String, String> map = (Map<String, String>) value;
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.writeValueAsString(map);
                } catch (JsonProcessingException e) {
                    throw new OpenOCommandInvalidParameterValue(this.getName());
                }
            }

            return value;
        }
        return getDefaultValue();
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public boolean isSecured() {
        return isSecured;
    }

    public void setSecured(boolean isSecured) {
        this.isSecured = isSecured;
    }

    public static String printShortOption(String option) {
        return "-" + option;
    }

    public static String printLongOption(String option) {
        return "-" + printShortOption(option);
    }

    /**
     * Validate parameter value.
     *
     * @throws OpenOCommandParameterMissing
     *             exception
     * @throws OpenOCommandInvalidParameterValue exception
     */
    public void validate() throws OpenOCommandParameterMissing, OpenOCommandInvalidParameterValue {
        // (mrkanag) empty check needs to revisit
        if (!this.isOptional() && (this.getValue() == null || this.getValue().toString().isEmpty())) {
            throw new OpenOCommandParameterMissing(this.getName());
        }

        // (mrkanag) validate for type supported ParameterType using constraints
    }
}
