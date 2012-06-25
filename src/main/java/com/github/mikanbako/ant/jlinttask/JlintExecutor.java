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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This object executes Jlint.
 *
 * This class is independent with Ant.
 */
/* package */ final class JlintExecutor {
    /**
     * Path of executable file of Jlint.
     */
    private final File mJlintExecutable;

    /**
     * List of class file.
     */
    private final ArrayList<File> mClassFiles;

    /**
     * Path of directory that contains source files.
     */
    private File mSourceDirectory;

    /**
     * Set of option.
     */
    private final HashSet<String> mOptions = new HashSet<String>();

    /**
     * Object that executes command.
     */
    private final CommandExecutor mCommandExecutor;

    /**
     * Constructor.
     *
     * @param jlintExecutable Jlint executable file
     * @param classFiles Class files
     */
    public JlintExecutor(File jlintExecutable, List<File> classFiles) {
        this(new ProcessExecutor(), jlintExecutable, classFiles);
    }

    /**
     * Constructor with {@link CommandExecutor}.
     *
     * @param executor Object that executes command
     * @param jlintExecutable Jlint executable file
     * @param classFiles List of class file
     * @throws IllegalArgumentException Argument is null
     */
    /* package */ JlintExecutor(CommandExecutor executor,
            File jlintExecutable, List<File> classFiles) {
        if (executor == null || jlintExecutable == null || classFiles == null) {
            throw new IllegalArgumentException("Argument is null.");
        }

        mCommandExecutor = executor;
        mJlintExecutable = jlintExecutable;
        mClassFiles = new ArrayList<File>(classFiles);
    }

    /**
     * Set path of directory that contains source files.
     *
     * @param sourceDirectory Path of directory that contains source files
     */
    public void setSourceDirectory(File sourceDirectory) {
        mSourceDirectory = sourceDirectory;
    }

    /**
     * Set options.
     *
     * The option does not have its argument.
     *
     * @param options Set of options.
     */
    public void setOptions(Set<String> options) {
        mOptions.clear();
        mOptions.addAll(options);
    }

    /**
     * Execute Jlint.
     *
     * @param output {@link StringBuilder} that is appended for the standard
     *  output from Jlint
     * @param error {@link StringBuilder} that is appended for the standard
     *  error from Jlint
     * @return Exit code
     * @throws IOException If I/O error occurs
     * @throws InterruptedException If this thread is interrupted
     */
    public int execute(StringBuilder output, StringBuilder error) throws IOException,
            InterruptedException {
        ArrayList<String> command = new ArrayList<String>();

        command.add(mJlintExecutable.getAbsolutePath());

        if (mSourceDirectory != null) {
            command.add("-source");
            command.add(mSourceDirectory.getAbsolutePath());
        }

        for (String option : mOptions) {
            command.add(option);
        }

        for (File classFile : mClassFiles) {
            command.add(classFile.getAbsolutePath());
        }

        return mCommandExecutor.execute(command, output, error);
    }
}
