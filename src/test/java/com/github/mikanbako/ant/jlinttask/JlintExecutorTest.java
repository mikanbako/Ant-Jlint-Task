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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Test {@link JlintExecutor}.
 */
public class JlintExecutorTest extends TestCase {
    private static final File JLINT_EXECUTABLE = new File(".");

    private CommandRecordingExecutor mCommandExecutor;

    private StringBuilder mResultStringBuilder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mCommandExecutor = new CommandRecordingExecutor();
        mResultStringBuilder = new StringBuilder();
    }

    /**
     * Create {@link JlintExecutor}.
     *
     * @param jlintExecutable Jlint executable file
     * @param classFiles Class files
     * @return {@link JlintExecutor}
     */
    private JlintExecutor createJlintExecutor(
            File jlintExecutable, List<File> classFiles) {
        return new JlintExecutor(
                mCommandExecutor, jlintExecutable, classFiles);
    }

    /**
     * Test with minimum arguments.
     *
     * Command is "<Jlint executable> <class file> ...".
     */
    @Test
    public void testMinimumArguments() throws Exception {
        List<File> classFiles = Arrays.asList(new File("a"), new File("b"));

        JlintExecutor executor = createJlintExecutor(
                JLINT_EXECUTABLE, classFiles);
        executor.execute(mResultStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                JLINT_EXECUTABLE.getAbsolutePath(),
                recordedCommand.get(0));
        assertEquals(
                classFiles.get(0).getAbsolutePath(),
                recordedCommand.get(1));
        assertEquals(
                classFiles.get(0).getAbsolutePath(),
                recordedCommand.get(1));
    }


    /**
     * Test with source directory.
     *
     * Command is "<Jlint executable> -source <source directory>
     *  <class file> ...".
     */
    @Test
    public void testWithSourceDirectory() throws Exception {
        File sourceDirectory = new File("d");
        List<File> classFiles = Collections.singletonList(new File("a"));

        JlintExecutor executor = createJlintExecutor(
                JLINT_EXECUTABLE, classFiles);
        executor.setSourceDirectory(sourceDirectory);
        executor.execute(mResultStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                JLINT_EXECUTABLE.getAbsolutePath(), recordedCommand.get(0));
        assertEquals("-source", recordedCommand.get(1));
        assertEquals(
                sourceDirectory.getAbsolutePath(), recordedCommand.get(2));
        assertEquals(
                classFiles.get(0).getAbsolutePath(), recordedCommand.get(3));
    }

    /**
     * Test with some options.
     *
     * Command is "<Jlint executable> <option> ... <class file> ...".
     */
    @Test
    public void testWithOptions() throws Exception {
        List<File> classFiles = Collections.singletonList(new File("a"));
        HashSet<String> options = new HashSet<String>();
        options.add("+all");
        options.add("-data_flow");

        JlintExecutor executor = createJlintExecutor(JLINT_EXECUTABLE,
                classFiles);
        executor.setOptions(options);
        executor.execute(mResultStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                JLINT_EXECUTABLE.getAbsolutePath(), recordedCommand.get(0));
        assertTrue(recordedCommand.subList(1, 3).contains("+all"));
        assertTrue(recordedCommand.subList(1, 3).contains("-data_flow"));
        assertEquals(classFiles.get(0).getAbsolutePath(),
                recordedCommand.get(3));
    }
}
