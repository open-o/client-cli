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

import org.openo.client.cli.fw.error.OpenOCommandInvalidResultAttributeScope;

/**
 * OpenO command supports to print output with given set of defined attributes, and each attributes are marked with this
 * visibility.
 *
 */
public enum OpenOCommandResultAttributeScope {
    /**
     * By default, all output attributes which are tagged with short would be printed.
     */
    SHORT,
    /**
     * When user provides --long or -l, all attributes including short tagged will be printed. otherwise, attributes
     * tagged with long, whould be ignored as part of output
     */
    LONG;

    /**
     * get attribute scope enum type.
     *
     * @param name
     *            scope
     * @return type
     * @throws OpenOCommandInvalidResultAttributeScope
     *             exception
     */
    public static OpenOCommandResultAttributeScope get(String name) throws OpenOCommandInvalidResultAttributeScope {
        if (name.equals(LONG.name().toLowerCase())) {
            return LONG;
        } else if (name.equals(SHORT.name().toLowerCase())) {
            return SHORT;
        }

        throw new OpenOCommandInvalidResultAttributeScope(name);
    }
}
