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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.openo.client.cli.fw.error.OpenOCommandOutputPrintingFailed;
import org.openo.client.cli.fw.output.PrintDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenOCommandPrintTest {

    @Test
    @Ignore
    public void printCsvTest() throws OpenOCommandOutputPrintingFailed {
        OpenOCommandPrint pr = new OpenOCommandPrint();
        pr.setDirection(PrintDirection.LANDSCAPE);
        pr.setPrintTitle(true);
        pr.addColumn("name1", new ArrayList<String>(Arrays.asList(new String[] { "value1" })));
        String exp = "name1\r\n";
        String result = pr.printCsv();
        assertEquals(exp, result);
    }

    @Test
    public void printTableTest() throws OpenOCommandOutputPrintingFailed {
        OpenOCommandPrint pr = new OpenOCommandPrint();
        List<String> getColumnsData = new ArrayList<String>();
        pr.setDirection(PrintDirection.LANDSCAPE);
        pr.setPrintTitle(true);
        pr.addColumn("name2", new ArrayList<String>(Arrays.asList(new String[] { "value2" })));
        String exp = "+--------+\n|name2   |\n+--------+\n|value2  |\n+--------+\n";
        String result = pr.printTable(true);
        getColumnsData = pr.getColumn("name2");
        assertEquals(exp, result);
    }

    @Test
    public void printTableNullColumnHeaderTest() throws OpenOCommandOutputPrintingFailed {
        OpenOCommandPrint pr = new OpenOCommandPrint();
        List<String> getColumnsData = new ArrayList<String>();
        pr.setDirection(PrintDirection.LANDSCAPE);
        pr.setPrintTitle(true);
        pr.addColumn("name2", new ArrayList<String>(Arrays.asList(new String[] { "value2" })));
        String exp = "+--------+\n|name2   |\n+--------+\n|value2  |\n+--------+\n";
        String result = pr.printTable(true);
        getColumnsData = pr.getColumn(null);
        assertEquals(exp, result);
    }

    @Test
    public void printTableEmptyColumnValuesTest() throws OpenOCommandOutputPrintingFailed {
        OpenOCommandPrint pr = new OpenOCommandPrint();
        List<String> getColumnsData = new ArrayList<String>();
        pr.setDirection(PrintDirection.LANDSCAPE);
        pr.setPrintTitle(true);
        pr.addColumn("name2", new ArrayList<String>(Arrays.asList(new String[] { "" })));
        String exp = "+--------+\n|name2   |\n+--------+\n|        |\n+--------+\n";
        String result = pr.printTable(true);
        getColumnsData = pr.getColumn("name2");
        assertEquals(exp, result);
    }
}
