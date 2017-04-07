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

package org.openo.client.cli.fw;

import org.openo.client.cli.fw.cmd.OpenOHttpCommand;
import org.openo.client.cli.fw.conf.Constants;
import org.openo.client.cli.fw.conf.OpenOCommandConfg;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandHelpFailed;
import org.openo.client.cli.fw.error.OpenOCommandInvalidRegistration;
import org.openo.client.cli.fw.error.OpenOCommandNotFound;
import org.openo.client.cli.fw.error.OpenOCommandRegistrationFailed;
import org.openo.client.cli.fw.output.OpenOCommandResult;
import org.openo.client.cli.fw.output.OpenOCommandResultAttribute;
import org.openo.client.cli.fw.output.OpenOCommandResultAttributeScope;
import org.openo.client.cli.fw.output.PrintDirection;
import org.openo.client.cli.fw.output.ResultType;
import org.openo.client.cli.fw.utils.ExternalSchema;
import org.openo.client.cli.fw.utils.OpenOCommandUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OpenO Command registrar provides a common place, where every command would get registered automatically when its
 * loaded into JVM.
 *
 */
public class OpenOCommandRegistrar {
    /*
     * static { //Start the AOP for logging new OpenOCommandLogger(); }
     */
    private Map<String, Class<? extends OpenOCommand>> registry = new HashMap<>();

    private static OpenOCommandRegistrar registrar = null;

    /**
     * Register the command into registrar and throws OpenOInvalidCommandRegistration for invalid command.
     *
     * @param name
     *            Command Name
     * @param cmd
     *            Command Class
     * @throws OpenOCommandInvalidRegistration
     *             Invalid registration exception
     */
    public void register(String name, Class<? extends OpenOCommand> cmd) throws OpenOCommandInvalidRegistration {
        this.registry.put(name, cmd);
    }

    /**
     * Get global registrar.
     *
     * @throws OpenOCommandException
     *             exception
     */
    public static OpenOCommandRegistrar getRegistrar() throws OpenOCommandException {
        if (registrar == null) {
            registrar = new OpenOCommandRegistrar();
            registrar.autoDiscover();
            registrar.autoDiscoverHttpSchemas();
        }

        return registrar;
    }

    /**
     * Get the list of discovered commands by registrar.
     *
     * @return set
     */
    public Set<String> listCommands() {
        return this.registry.keySet();
    }

    /**
     * Returns map of command to schema.
     *
     * @return map
     * @throws OpenOCommandException
     *             exception
     */
    public Map<String, String> getAllCommandToSchemaMap() throws OpenOCommandException {
        Map<String, String> map = new HashMap<>();
        List<ExternalSchema> schemas = OpenOCommandUtils.findAllExternalSchemas();
        if (schemas != null) {
            for (ExternalSchema schema : schemas) {
                map.put(schema.getCmdName(), schema.getSchemaName());
            }
        }
        if (this.registry != null) {
            for (String cmd : this.registry.keySet()) {
                if (!map.containsKey(cmd) && registry.get(cmd) != null) {
                    map.put(cmd, this.getSchemaFileName(registry.get(cmd)));
                }
            }
        }

        return map;
    }

    /**
     * Get the OpenOCommand, which CLI main would use to find the command based on the command name.
     *
     * @param cmdName
     *            Name of command
     * @return OpenOCommand
     * @throws OpenOCommandException
     *             Exception
     */
    public OpenOCommand get(String cmdName) throws OpenOCommandException {
        OpenOCommand cmd;
        Class<? extends OpenOCommand> cls = registry.get(cmdName);
        if (cls == null) {
            throw new OpenOCommandNotFound(cmdName);
        }

        try {
            Constructor<?> constr = cls.getConstructor();
            cmd = (OpenOCommand) constr.newInstance();

            String schemaName;
            if (cmd.getClass().equals(OpenOHttpCommand.class)) { // NOSONAR
                schemaName = OpenOCommandUtils.loadExternalSchemaFromJson(cmdName).getSchemaName();
            } else {
                schemaName = this.getSchemaFileName(cls);
            }
            cmd.initializeSchema(schemaName);
        } catch (OpenOCommandException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new OpenOCommandRegistrationFailed(cmdName, e);
        }

        return cmd;
    }

    private void autoDiscover() throws OpenOCommandInvalidRegistration {
        List<Class<OpenOCommand>> cmds = OpenOCommandUtils.findOpenOCommands();

        for (Class<OpenOCommand> cmd : cmds) {
            if (cmd.isAnnotationPresent(OpenOCommandSchema.class)) {
                OpenOCommandSchema ano = cmd.getAnnotation(OpenOCommandSchema.class);
                this.register(ano.name(), cmd);
            }
        }
    }

    private void autoDiscoverHttpSchemas() throws OpenOCommandException {
        List<ExternalSchema> schemas = OpenOCommandUtils.loadExternalSchemasFromJson();
        for (ExternalSchema schema : schemas) {
            this.register(schema.getCmdName(), OpenOHttpCommand.class);
        }
    }

    private String getSchemaFileName(Class<? extends OpenOCommand> cmd) {
        OpenOCommandSchema ano = (OpenOCommandSchema) cmd.getAnnotation(OpenOCommandSchema.class);
        if (ano.schema().isEmpty()) {
            return "openo-" + ano.name() + "-schema.yaml";
        }
        return ano.schema();
    }

    /**
     * Helps to find the Open-O CLI version, could be used with --version or -v option.
     *
     * @return string
     */
    public String getVersion() {
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = OpenOCommandConfg.getVersion();
        }
        return version;
    }

    /**
     * Provides the help message in tabular format for all commands registered in this registrar.
     *
     * @return string
     * @throws OpenOCommandHelpFailed
     *             Help cmd failed
     */
    public String getHelp() throws OpenOCommandHelpFailed {
        OpenOCommandResult help = new OpenOCommandResult();
        help.setType(ResultType.TABLE);
        help.setPrintDirection(PrintDirection.LANDSCAPE);

        OpenOCommandResultAttribute attr = new OpenOCommandResultAttribute();
        attr.setName(Constants.NAME.toUpperCase());
        attr.setDescription(Constants.DESCRIPTION);
        attr.setScope(OpenOCommandResultAttributeScope.SHORT);
        help.getRecords().add(attr);

        OpenOCommandResultAttribute attrSrv = new OpenOCommandResultAttribute();
        attrSrv.setName(Constants.SERVICE.toUpperCase());
        attrSrv.setDescription(Constants.SERVICE);
        attrSrv.setScope(OpenOCommandResultAttributeScope.SHORT);
        help.getRecords().add(attrSrv);

        OpenOCommandResultAttribute attrDesc = new OpenOCommandResultAttribute();
        attrDesc.setName(Constants.DESCRIPTION.toUpperCase());
        attrDesc.setDescription(Constants.DESCRIPTION);
        attrDesc.setScope(OpenOCommandResultAttributeScope.SHORT);
        help.getRecords().add(attrDesc);

        for (String cmdName : OpenOCommandUtils.sort(this.listCommands())) {
            OpenOCommand cmd;
            try {
                cmd = this.get(cmdName);
            } catch (OpenOCommandException e) {
                throw new OpenOCommandHelpFailed(e);
            }

            attr.getValues().add(cmd.getName());
            attrSrv.getValues().add(cmd.printVersion());
            attrDesc.getValues().add(cmd.getDescription());
        }

        try {
            return "\n\nOpen-O sub-commands:\n" + help.print();
        } catch (OpenOCommandException e) {
            throw new OpenOCommandHelpFailed(e);
        }
    }
}
