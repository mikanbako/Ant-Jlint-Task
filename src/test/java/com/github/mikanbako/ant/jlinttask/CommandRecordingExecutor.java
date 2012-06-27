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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command recording implementation of {@link CommandExecutor}.
 */
/* package */ class CommandRecordingExecutor implements CommandExecutor {
    /**
     * Recorded command.
     */
    private final ArrayList<String> mRecordedCommand = new ArrayList<String>();

    /**
     * Exit code.
     */
    private int mExitCode;

    @Override
    public int execute(List<String> command, StringBuilder ignoredOutput,
            StringBuilder ignoredError) throws IOException,
            InterruptedException {
        mRecordedCommand.clear();
        mRecordedCommand.addAll(command);

        return mExitCode;
    }

    /**
     * Set exit code for {@link #execute(List)}.
     *
     * Default is 0.
     *
     * @param exitCode Exit code
     */
    public void setExitCode(int exitCode) {
        mExitCode = exitCode;
    }

    /**
     * Get recorded command.
     *
     * @return Unmodifiable list that contains recorded command
     */
    public List<String> getRecordedCommand() {
        return Collections.unmodifiableList(mRecordedCommand);
    }
}
