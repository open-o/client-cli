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

package org.openo.client.cli.fw.output;

/**
 * Open-O command result format.
 *
 */
public enum ResultType {
    TABLE, CSV, JSON, YAML, TEXT;

    /**
     * Check whether the output to be formatted in tabular format.
     *
     * @param type
     *            output format type
     * @return boolean
     */
    public static boolean isTabularForm(String type) {
        if (type.equalsIgnoreCase(TABLE.name()) || type.equalsIgnoreCase(TABLE.name())) {
            return true;
        }

        return false;
    }

    /**
     * Get ResultType.
     *
     * @param name
     *            format name
     * @return ResultType
     */
    public static ResultType get(String name) {
        if (name.toUpperCase().equals(TABLE.name())) {
            return TABLE;
        }
        if (name.toUpperCase().equals(CSV.name())) {
            return CSV;
        }
        if (name.toUpperCase().equals(JSON.name())) {
            return JSON;
        }
        if (name.toUpperCase().equals(YAML.name())) {
            return YAML;
        }
        return TEXT;
    }
}