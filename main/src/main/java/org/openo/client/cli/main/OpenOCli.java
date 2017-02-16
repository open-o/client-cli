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

package org.openo.client.cli.main;

import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.OpenOCommandRegistrar;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.output.OpenOCommandResult;
import org.openo.client.cli.main.conf.OpenOCliConstants;
import org.openo.client.cli.main.utils.OpenOCliUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Open-O Command Line Interface (CLI).
 *
 */
public class OpenOCli {

    private List<String> args = new ArrayList<>();

    private int exitCode = -1;

    public OpenOCli(String[] args) {
        this.args = Arrays.asList(args);
    }

    private void exitSuccessfully() {
        this.exitCode = OpenOCliConstants.EXIT_SUCCESS;
    }

    private void exitFailure() {
        this.exitCode = OpenOCliConstants.EXIT_FAILURE;
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private String getShortOption(String opt) {
        return OpenOCommandParameter.printShortOption(opt);
    }

    private String getLongOption(String opt) {
        return OpenOCommandParameter.printLongOption(opt);
    }

    public int getExitCode() {
        return this.exitCode;
    }

    /**
     * Handles help. --help or -h
     */
    public void handleHelp() {
        try {
            // By default, it prints help
            if ((args.size() == 0)
                    || ((args.size() == 1) && (this.getLongOption(OpenOCliConstants.PARAM_HELP_LOGN).equals(args.get(0))
                            || this.getShortOption(OpenOCliConstants.PARAM_HELP_SHORT).equals(args.get(0))))) {
                String help = OpenOCommandRegistrar.getRegistrar().getHelp();
                this.print(help);
                this.exitSuccessfully();
            }
        } catch (Exception e) {
            this.print(e.getMessage());
            this.exitFailure();
        }
    }

    /**
     * Handles version. --version or -v
     */
    public void handleVersion() {
        try {
            if ((args.size() == 1) && (this.getLongOption(OpenOCliConstants.PARAM_VERSION_LONG).equals(args.get(0))
                    || this.getShortOption(OpenOCliConstants.PARAM_VERSION_SHORT).equals(args.get(0)))) {
                String version = OpenOCommandRegistrar.getRegistrar().getVersion();
                this.print(version);
                this.exitSuccessfully();
            }
        } catch (Exception e) {
            this.print(e.getMessage());
            this.exitFailure();
        }
    }

    /**
     * Handles command.
     */
    public void handleCommand() {
        try {
            if (!args.isEmpty()) {
                OpenOCommand cmd = OpenOCommandRegistrar.getRegistrar().get(args.get(0));
                // check for help or version
                if (args.size() == 2) {
                    if ((this.getLongOption(OpenOCliConstants.PARAM_HELP_LOGN).equals(args.get(1))
                            || this.getShortOption(OpenOCliConstants.PARAM_HELP_SHORT).equals(args.get(1)))) {
                        String help = cmd.printHelp();
                        this.print(help);
                        this.exitSuccessfully();
                        return;
                    } else if ((this.getLongOption(OpenOCliConstants.PARAM_VERSION_LONG).equals(args.get(1))
                            || this.getShortOption(OpenOCliConstants.PARAM_VERSION_SHORT).equals(args.get(1)))) {
                        String version = cmd.printVersion();
                        this.print(version);
                        this.exitSuccessfully();
                        return;
                    }
                }

                OpenOCliUtils.populateParams(cmd.getParameters(), args);
                OpenOCommandResult result = cmd.execute();
                this.print(result.getDebugInfo());
                this.print(result.print());
                this.exitSuccessfully();
            }
        } catch (Exception e) {
            this.print(e.getMessage());
            this.exitFailure();
        }
    }

    /**
     * Handles all client input.
     */
    public void handle() {
        this.handleHelp();
        if (this.exitCode == -1) {
            this.handleVersion();
            if (this.exitCode == -1) {
                this.handleCommand();
            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            array
     */
    public static void main(String[] args) {
        OpenOCli cli = new OpenOCli(args);
        cli.handle();
        System.exit(cli.getExitCode());
    }

}
