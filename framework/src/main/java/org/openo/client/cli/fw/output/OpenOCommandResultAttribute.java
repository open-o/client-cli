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

import org.openo.client.cli.fw.input.ParameterType;

import java.util.ArrayList;
import java.util.List;

/**
 * Open-O command output records, helps to define the title and its description while command is defined and during run
 * time, it captures the value of the output as well.
 */
public class OpenOCommandResultAttribute {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SCOPE = "scope";
    public static final String TYPE = "type";
    public static final String IS_SECURED = "is_secured";

    /*
     * Output name
     */
    public String name;

    /*
     * Output description
     */
    public String description;

    /*
     * Output values, in case list out, it holds values for all rows for show output, it will have one value
     */
    public List<String> values = new ArrayList<>();

    /*
     * Output scope
     */
    public OpenOCommandResultAttributeScope scope = OpenOCommandResultAttributeScope.SHORT;

    private ParameterType type = ParameterType.STRING;

    private boolean isSecured = false;

    public void setValues(List<String> values) {
        this.values = values;
    }

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

    public List<String> getValues() {
        return values;
    }

    public OpenOCommandResultAttributeScope getScope() {
        return scope;
    }

    public void setScope(OpenOCommandResultAttributeScope scope) {
        this.scope = scope;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public boolean isSecured() {
        return isSecured;
    }

    public void setSecured(boolean isSecured) {
        this.isSecured = isSecured;
    }

}
