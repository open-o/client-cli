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

import org.openo.client.cli.fw.error.OpenOCommandParameterMissing;

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
    public String name;

    /*
     * Description
     */
    public String description = "";

    /*
     * Short Option, like -f, for positional parameters, its not required.
     */
    public String shortOption = null;

    /*
     * Long Option, like --file-path, for positional parameters, its not required.
     */
    public String longOption = null;

    /*
     * Parameter type such as int, json, yaml, string, etc
     */
    public ParameterType parameterType;

    /*
     * Default value
     */
    public String defaultValue = "";

    /*
     * Is optional
     */
    public boolean isOptional = false;

    /*
     * Is secured
     */
    public boolean isSecured = false;

    /*
     * Parameter Value
     */
    public String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     */
    public String getValue() {
        if (value != null) {
            return value;
        }
        return getDefaultValue();
    }

    public void setValue(String value) {
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
     */
    public void validate() throws OpenOCommandParameterMissing {
        if (!this.isOptional()) {
            if (this.getValue() == null || this.getValue().isEmpty()) {
                throw new OpenOCommandParameterMissing(this.getName());
            }
        }

        // TODO(mrkanag): validate for type supported ParameterType using constraints

        if (this.getParameterType().equals(ParameterType.JSON) || this.getParameterType().equals(ParameterType.YAML)) {
            // TODO(mrkanag): On type=JSON/YAML, successful validation, set the value to the content of file
        }
    }
}
