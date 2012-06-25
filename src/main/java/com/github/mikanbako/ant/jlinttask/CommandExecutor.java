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

import java.io.IOException;
import java.util.List;

/**
 * This class executes command.
 */
/* package */ interface CommandExecutor {
    /**
     * Execute a command.
     *
     * @param command Program and its arguments
     * @param output {@link StringBuilder} that is appended for the standard
     *  output from command
     * @param error {@link StringBuilder} that is appended for the standard
     *  error from command
     * @return Exit code of program.
     * @throws IOException If I/O error occurs
     * @throws InterruptedException If other thread is interrupted.
     */
    int execute(List<String> command, StringBuilder output, StringBuilder error) throws IOException, InterruptedException;
}
