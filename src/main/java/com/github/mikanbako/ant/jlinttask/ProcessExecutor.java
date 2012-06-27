/* Jlint Ant Task
    Copyright (C) 2012 Keita Kita

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
