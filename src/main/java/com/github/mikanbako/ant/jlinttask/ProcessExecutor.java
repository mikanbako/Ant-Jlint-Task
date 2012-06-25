/*
 * Copyright 2012 Keita Kita
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mikanbako.ant.jlinttask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Implementation of {@link CommandExecutor} executes as subprocess.
 */
/* package */ final class ProcessExecutor implements CommandExecutor {
    /**
     * Line separator on this platform.
     */
    private static final String LINE_SEPARATOR =
            System.getProperty("line.separator");


    @Override
    public int execute(List<String> command,
            StringBuilder output, StringBuilder error) throws
            IOException, InterruptedException {
        // Execute command.

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        process.waitFor();

        // Extract output from command.

        read(process.getInputStream(), output);
        read(process.getErrorStream(), error);

        return process.exitValue();
    }

    /**
     * Read string from the {@link InputStream}.
     *
     * @param inputStream {@link InputStream} to read string
     * @param destination Destination of the string
     * @throws IOException If I/O error occurs
     */
    private void read(InputStream inputStream,
            StringBuilder destination) throws IOException {
        LineNumberReader reader = new LineNumberReader(
                new BufferedReader(
                        new InputStreamReader(
                                inputStream,
                                Charset.defaultCharset())));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                destination.append(line);
                destination.append(LINE_SEPARATOR);
            }
        } finally {
            reader.close();
        }
    }
}
