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

import org.openo.client.cli.fw.conf.OpenOCommandConfg;
import org.openo.client.cli.fw.error.OpenOCommandOutputFormatNotsupported;
import org.openo.client.cli.fw.error.OpenOCommandOutputPrintingFailed;
import org.openo.client.cli.fw.input.ParameterType;
import org.openo.client.cli.fw.output.print.OpenOCommandPrint;
import org.openo.client.cli.fw.utils.OpenOCommandUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenO Command result holds the final output of the command.
 *
 */
public class OpenOCommandResult {
    public static final String DIRECTION = "direction";
    public static final String ATTRIBUTES = "attributes";

    /*
     * if type=JSON, then JSON response of the command from back-end Open-O service, by default all the command would
     * set this value once the back-end call returns, which would be useful to print the output in JSON format, returned
     * from the back-end service.
     *
     * if type=TEXT, then it holds the result in text format such as help message
     */
    private Object output;

    /*
     * Type requested by user
     */
    private ResultType type = ResultType.TABLE;

    /*
     * Scope requested by user
     */
    private OpenOCommandResultAttributeScope scope = OpenOCommandResultAttributeScope.SHORT;

    /*
     * if type=TABLE, then List of result records, which could be printed on the CLI console, loaded from schema file
     */
    private List<OpenOCommandResultAttribute> records = new ArrayList<>();

    /*
     * Print horizontally or vertically, Mostly for show command, horizontal table while for list commands , it will be
     * vertically printed. Respective command should set appropriately.
     *
     * loaded from schema file
     */
    private PrintDirection printDirection = PrintDirection.LANDSCAPE;

    private String debugInfo = "";

    /**
     * Requested by user.
     */
    private boolean includeTitle = true;

    /**
     * Requested by user.
     */
    private boolean includeSeparator = true;

    /**
     * Requested by user.
     */
    private boolean isDebug = false;

    public PrintDirection getPrintDirection() {
        return printDirection;
    }

    public void setPrintDirection(PrintDirection printDirection) {
        this.printDirection = printDirection;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public List<OpenOCommandResultAttribute> getRecords() {
        return records;
    }

    public void setRecords(List<OpenOCommandResultAttribute> records) {
        this.records = records;
    }

    /**
     * Record mapping.
     *
     * @return attributes
     */
    public Map<String, OpenOCommandResultAttribute> getRecordsMap() {
        Map<String, OpenOCommandResultAttribute> recordMap = new HashMap<>();

        for (OpenOCommandResultAttribute record : this.getRecords()) {
            recordMap.put(record.getName(), record);
        }

        return recordMap;
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }

    public OpenOCommandResultAttributeScope getScope() {
        return scope;
    }

    public void setScope(OpenOCommandResultAttributeScope scope) {
        this.scope = scope;
    }

    public boolean isIncludeTitle() {
        return includeTitle;
    }

    public void setIncludeTitle(boolean includeTitle) {
        this.includeTitle = includeTitle;
    }

    public boolean isIncludeSeparator() {
        return includeSeparator;
    }

    public void setIncludeSeparator(boolean includeSeparator) {
        this.includeSeparator = includeSeparator;
    }

    public String getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * Helps to print the result based on the type.
     *
     * @return string
     * @throws OpenOCommandOutputFormatNotsupported
     *             excpetion
     * @throws OpenOCommandOutputPrintingFailed
     *             exception
     */
    public String print() throws OpenOCommandOutputFormatNotsupported, OpenOCommandOutputPrintingFailed {
        String printOutput = "";

        if (this.getRecords().isEmpty()) {
            return printOutput;
        }

        OpenOCommandPrint print = new OpenOCommandPrint();
        print.setPrintTitle(this.isIncludeTitle());
        if (this.getPrintDirection().equals(PrintDirection.LANDSCAPE)) {
            for (OpenOCommandResultAttribute record : this.getScopedRecords()) {
                if (record.getType().equals(ParameterType.JSON)) {
                    print.addColumn(record.getName(), OpenOCommandUtils.jsonFlatten(record.getValues()));
                } else {
                    print.addColumn(record.getName(), record.getValues());
                }
            }
        } else {
            // Add property column
            OpenOCommandResultAttribute prp = new OpenOCommandResultAttribute();
            prp.setName(OpenOCommandConfg.PORTRAINT_COLUMN_NAME_PROPERTY);
            prp.setScope(OpenOCommandResultAttributeScope.SHORT);
            // Add value column
            OpenOCommandResultAttribute val = new OpenOCommandResultAttribute();
            val.setName(OpenOCommandConfg.PORTRAINT_COLUMN_NAME_VALUE);
            val.setScope(OpenOCommandResultAttributeScope.SHORT);

            for (OpenOCommandResultAttribute record : this.getScopedRecords()) {
                prp.getValues().add(record.getName());
                if (record.getValues().size() == 1) {
                    val.getValues().add(record.getValues().get(0));
                } else {
                    val.getValues().add(record.getValues().toString());
                }
            }

            print.addColumn(prp.getName(), prp.getValues());
            print.addColumn(val.getName(), val.getValues());
        }

        if (this.isDebug()) {
            printOutput = this.getDebugInfo() + "\n";
        }

        if (this.getType().equals(ResultType.JSON)) {
            return printOutput + print.printJson();
        } else if (this.getType().equals(ResultType.TABLE)) {
            return printOutput + print.printTable(this.isIncludeSeparator());
        } else if (this.getType().equals(ResultType.CSV)) {
            return printOutput + print.printCsv();
        }

        throw new OpenOCommandOutputFormatNotsupported(this.getType().name());
    }

    private List<OpenOCommandResultAttribute> getScopedRecords() {
        List<OpenOCommandResultAttribute> recordList = new ArrayList<>();
        for (OpenOCommandResultAttribute record : this.getRecords()) {
            if (record.getScope().ordinal() > this.getScope().ordinal()) {
                continue;
            }
            recordList.add(record);
        }

        return recordList;
    }
}
