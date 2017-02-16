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

/**
 * Open-O Service as reported in Open-O MSB like msb v1 or /api/microservice/v1.
 */
public class OpenOService {
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String BASE_PATH = "base_path";
    /*
     * OpenO Service name like gso.
     */
    private String name;

    /*
     * OpenO Service API version like v1, v2, etc
     */
    private String version;

    private String basePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String toString() {
        return this.getName() + " " + this.getVersion();
    }

}
