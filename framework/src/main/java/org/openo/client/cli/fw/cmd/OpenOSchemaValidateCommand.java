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

package org.openo.client.cli.fw.cmd;

import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.OpenOCommandSchema;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.schema.SchemaValidate;
import org.openo.client.cli.fw.schema.SchemaValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validate schema command.
 *
 */
@OpenOCommandSchema(name = "schema-validate", schema = "schema-validate.yaml")
public class OpenOSchemaValidateCommand extends OpenOCommand {

    @Override
    protected void run() throws OpenOCommandException {
        Map<String, OpenOCommandParameter> paramMap = getParametersMap();
        OpenOCommandParameter locationParam = paramMap.get("schema-location");
        String location = String.valueOf(locationParam.getValue());
        OpenOCommandParameter interSchemaParam = paramMap.get("internal-schema");
        boolean isInternalSchema = Boolean.valueOf(String.valueOf(interSchemaParam.getValue()));
        SchemaValidate schema;
        if (isInternalSchema) {
            location = location.substring(1);
            schema = new SchemaValidator(location);
        } else {
            schema = new SchemaValidator(new File(location));
        }

        List<String> error = schema.validate();
        List<String> slNumber = new ArrayList<>();
        for (int i = 1; i <= error.size(); i++) {
            slNumber.add(String.valueOf(i));
        }
        this.getResult().getRecords().get(0).setValues(slNumber);
        this.getResult().getRecords().get(1).setValues(error);
    }

}
