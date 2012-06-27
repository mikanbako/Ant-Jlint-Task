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
    int execute(List<String> command,
            StringBuilder output, StringBuilder error) throws
            IOException, InterruptedException;
}
