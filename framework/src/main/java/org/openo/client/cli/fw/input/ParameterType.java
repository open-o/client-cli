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

import org.openo.client.cli.fw.error.OpenOCommandInvalidParameterType;

/**
 * Parameter type supported by Open-O CLI.
 *
 */
public enum ParameterType {
    /**
     * JSON file.
     */
    JSON,
    /**
     * YAML file.
     */
    YAML, STRING, LONG,
    /**
     * URL location.
     */
    URL, BOOL;

    /**
     * Get parameter type.
     *
     * @param name
     *            type name
     * @return type
     * @throws OpenOCommandInvalidParameterType
     *             exception
     */
    public static ParameterType get(String name) throws OpenOCommandInvalidParameterType {
        if (JSON.name().toLowerCase().equals(name)) {
            return JSON;
        } else if (YAML.name().toLowerCase().equals(name)) {
            return YAML;
        } else if (STRING.name().toLowerCase().equals(name)) {
            return STRING;
        } else if (LONG.name().toLowerCase().equals(name)) {
            return LONG;
        } else if (URL.name().toLowerCase().equals(name)) {
            return URL;
        } else if (BOOL.name().toLowerCase().equals(name)) {
            return BOOL;
        } else {
            throw new OpenOCommandInvalidParameterType(name);
        }
    }
}
