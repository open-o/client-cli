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

import org.apache.commons.io.IOUtils;
import org.openo.client.cli.fw.OpenOCommand;
import org.openo.client.cli.fw.OpenOCommandRegistrar;
import org.openo.client.cli.fw.error.OpenOCommandException;
import org.openo.client.cli.fw.error.OpenOCommandWarning;
import org.openo.client.cli.fw.input.OpenOCommandParameter;
import org.openo.client.cli.fw.output.OpenOCommandResult;
import org.openo.client.cli.main.conf.OpenOCliConstants;
import org.openo.client.cli.main.interactive.StringCompleter;
import org.openo.client.cli.main.utils.OpenOCliUtils;

import jline.TerminalFactory;
import jline.console.ConsoleReader;

import java.io.IOException;
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

    private void print(Throwable throwable) {
        System.out.println(throwable.getMessage());
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
            if ((args.isEmpty())
                    || ((args.size() == 1) && (this.getLongOption(OpenOCliConstants.PARAM_HELP_LOGN).equals(args.get(0))
                            || this.getShortOption(OpenOCliConstants.PARAM_HELP_SHORT).equals(args.get(0))))) {
                this.print(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("openo-readme.txt")));
                String help = OpenOCommandRegistrar.getRegistrar().getHelp();
                this.print(help);
                this.exitSuccessfully();
            }
        } catch (Exception e) {
            this.print(e);
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
            this.print(e);
            this.exitFailure();
        }
    }

    /**
     * Handles Interactive Mode. --interactive or -i
     */
    public void handleInteractive() { // NOSONAR
        if (isInteractive()) {
            ConsoleReader console = null;
            try {
                console = createConsoleReader();
                String line = null;
                while ((line = console.readLine()) != null) {
                    if (OpenOCliConstants.PARAM_INTERACTIVE_EXIT.equalsIgnoreCase(line)
                            || OpenOCliConstants.PARAM_INTERACTIVE_BYE.equalsIgnoreCase(line)) {
                        break;
                    } else if (OpenOCliConstants.PARAM_INTERACTIVE_CLEAR.equalsIgnoreCase(line)) {
                        console.clearScreen();
                        continue;
                    }
                    this.args = Arrays.asList(line.split(OpenOCliConstants.PARAM_INTERACTIVE_ARG_SPLIT_PATTERN));
                    handleCommand();
                }
            } catch (IOException e) { // NOSONAR
                this.print("Failed to read console, " + e.getMessage());
            } finally {
                try {
                    TerminalFactory.get().restore();
                } catch (Exception e) { // NOSONAR
                }
                if (console != null) {
                    console.close();
                }
                this.exitSuccessfully();
            }
        }
    }

    /**
     * Checks if the command is interactive.
     *
     * @return boolean
     */
    public boolean isInteractive() {
        if ((args.size() == 1) && (this.getLongOption(OpenOCliConstants.PARAM_INTERACTIVE_LONG).equals(args.get(0))
                || this.getShortOption(OpenOCliConstants.PARAM_INTERACTIVE_SHORT).equals(args.get(0)))) {
            return true;
        }

        return false;
    }

    /**
     * Creates console reader object.
     *
     * @return ConsoleReader
     * @throws IOException
     *             exception
     */
    private ConsoleReader createConsoleReader() throws IOException {
        ConsoleReader console = new ConsoleReader();
        try {
            StringCompleter strCompleter = new StringCompleter(OpenOCommandRegistrar.getRegistrar().listCommands());
            strCompleter.add(OpenOCliConstants.PARAM_INTERACTIVE_EXIT,
                    OpenOCliConstants.PARAM_INTERACTIVE_CLEAR);
            console.addCompleter(strCompleter);
            console.setPrompt(OpenOCliConstants.PARAM_INTERACTIVE_PROMPT);
        } catch (OpenOCommandException e) { // NOSONAR
            this.print("Failed to load openo commands," + e.getMessage());
        }

        return console;
    }

    /**
     * Handles command.
     */
    public void handleCommand() {
        OpenOCommand cmd;
        if (!args.isEmpty()) {
            try {
                cmd = OpenOCommandRegistrar.getRegistrar().get(args.get(0));
            } catch (Exception e) {
                this.print(e);
                this.exitFailure();
                return;
            }
            try {
                // check for help or version
                if (args.size() == 2) {
                    if (this.getLongOption(OpenOCliConstants.PARAM_HELP_LOGN).equals(args.get(1))
                            || this.getShortOption(OpenOCliConstants.PARAM_HELP_SHORT).equals(args.get(1))) {
                        String help = cmd.printHelp();
                        this.print(help);
                        this.exitSuccessfully();
                        return;
                    } else if (this.getLongOption(OpenOCliConstants.PARAM_VERSION_LONG).equals(args.get(1))
                            || this.getShortOption(OpenOCliConstants.PARAM_VERSION_SHORT).equals(args.get(1))) {
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
            } catch (Exception e) {
                this.print(cmd.getResult().getDebugInfo());
                if (e instanceof OpenOCommandWarning) {
                    this.exitSuccessfully();
                } else {
                    this.print(e);
                    this.exitFailure();
                }
            }
        }
    }

    /**
     * Handles all client input.
     */
    public void handle() {
        this.handleHelp();

        if (this.exitCode == -1) {
            this.handleVersion();
        }

        if (this.exitCode == -1) {
            this.handleInteractive();
        }

            if (this.exitCode == -1) {
                this.handleCommand();
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
