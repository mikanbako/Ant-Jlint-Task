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
