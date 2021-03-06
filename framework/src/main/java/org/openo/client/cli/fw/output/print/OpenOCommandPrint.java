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

package org.openo.client.cli.fw.output.print;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openo.client.cli.fw.error.OpenOCommandOutputPrintingFailed;
import org.openo.client.cli.fw.output.PrintDirection;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * OpenO Command Table print.
 *
 */
public class OpenOCommandPrint {

    public static final int MAX_COLUMN_LENGTH = 50;

    private PrintDirection direction;

    private Map<String, List<String>> data = new LinkedHashMap<>();

    private boolean printTitle = true;

    public PrintDirection getDirection() {
        return direction;
    }

    public void setDirection(PrintDirection direction) {
        this.direction = direction;
    }

    public void addColumn(String header, List<String> data) {
        this.data.put(header, data);
    }

    /**
     * Get column.
     *
     * @param header
     *            string
     * @return list
     */
    public List<String> getColumn(String header) {
        if (this.data.get(header) == null) {
            this.data.put(header, new ArrayList<String>());
        }
        return this.data.get(header);
    }

    public boolean isPrintTitle() {
        return printTitle;
    }

    public void setPrintTitle(boolean printTitle) {
        this.printTitle = printTitle;
    }

    private int findMaxRows() {
        int max = 1;
        if (!this.isPrintTitle()) {
            max = 0;
        }
        for (List<String> cols : this.data.values()) {
            if (cols != null && max < cols.size()) {
                max = cols.size();
            }
        }

        return max;
    }

    /**
     * Helps to form the rows from columns.
     *
     * @param isNormalize
     *            boolean
     * @return +--------------+-----------+-----------------------------+ | header1 | header 2 | header 3 |
     *         +--------------+-----------+-----------------------------+ | v1 | List[line| v 3 | | | 1, line2]| |
     *         +--------------+-----------+-----------------------------+ | null | yyyyyy 2 | xxxxxx 3 |
     *         +--------------+-----------+-----------------------------+
     */
    private List<List<Object>> formRows(boolean isNormalize) {
        List<List<Object>> rows = new ArrayList<>();

        // add title
        if (this.isPrintTitle()) {
            List<Object> list = new ArrayList<>();
            for (String key : this.data.keySet()) {
                if (isNormalize && key != null && key.length() > MAX_COLUMN_LENGTH) {
                    list.add(splitIntoList(key, MAX_COLUMN_LENGTH));
                } else {
                    list.add(key);
                }
            }
            rows.add(list);
        }

        // form row
        for (int i = 0; i < this.findMaxRows(); i++) {
            List<Object> row = new ArrayList<>();
            for (List<String> cols : this.data.values()) {
                if (cols.size() > i) {
                    String value = cols.get(i);
                    // split the cell into multiple sub rows
                    if (isNormalize && value != null && value.length() > MAX_COLUMN_LENGTH) {
                        row.add(splitIntoList(value, MAX_COLUMN_LENGTH));
                    } else {
                        // store as string (one entry)
                        row.add(value);
                    }
                } else {
                    // now value exist for this column
                    row.add(null);
                }
            }
            rows.add(row);
        }

        return rows;
    }

    /**
     * Splits big strings into list of strings based on maxCharInLine size.
     *
     * @param input
     *            input string
     * @param maxCharInLine
     *            max length
     * @return list of strings
     */
    public List<String> splitIntoList(String input, int maxCharInLine) {

        String inp = input;

        if (inp == null || "".equals(inp) || maxCharInLine <= 0) {
            return Collections.emptyList();
        }
        // new line is converted to space char
        if (inp.contains("\n")) {
            inp = inp.replaceAll("\n", "");
        }

        StringTokenizer tok = new StringTokenizer(inp, " ");
        StringBuilder output = new StringBuilder(inp.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while (word.length() >= maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
                word = word.substring(maxCharInLine - lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() >= maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        String[] strArray = output.toString().split("\n");

        return Arrays.asList(strArray);
    }

    /**
     * Helps to print table.
     *
     * @param printSeparator
     *            Prints with line separator
     * @return +--------------+-----------+-----------------------------+ | header1 | header 2 | header 3 |
     *         +--------------+-----------+-----------------------------+ | v1 | line 1 | v 3 | | | line 2 | |
     *         +--------------+-----------+-----------------------------+ | | yyyyyy 2 | xxxxxx 3 |
     *         +--------------+-----------+-----------------------------+
     */
    public String printTable(boolean printSeparator) {
        List<List<Object>> rows = this.formRows(true);
        TableGenerator table = new TableGenerator();
        return table.generateTable(rows, printSeparator);
    }

    /**
     * Print output in csv format.
     *
     * @return string
     * @throws OpenOCommandOutputPrintingFailed
     *             exception
     */
    public String printCsv() throws OpenOCommandOutputPrintingFailed {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = null;
        try {
            CSVFormat formattor = CSVFormat.DEFAULT.withRecordSeparator(System.getProperty("line.separator"));
            printer = new CSVPrinter(writer, formattor);

            List<List<Object>> rows = this.formRows(false);

            for (int i = 0; i < this.findMaxRows(); i++) {
                printer.printRecord(rows.get(i));
            }

            return writer.toString();
        } catch (IOException e) {
            throw new OpenOCommandOutputPrintingFailed(e);
        } finally {
            try {
                if (printer != null) {
                    printer.close();
                }
                writer.close();
            } catch (IOException e) {
                throw new OpenOCommandOutputPrintingFailed(e);  // NOSONAR
            }
        }
    }

    public String printJson() {
        // (mrkanag) print in json
        return null;
    }

    public String printYaml() {
        // (mrkanag) print in yaml
        return null;
    }
}
