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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Test {@link JlintExecutor}.
 */
public class JlintExecutorTest extends TestCase {
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
        File jlintExecutable = new File(".");
        List<File> classFiles = Arrays.asList(new File("a"), new File("b"));

        JlintExecutor executor = createJlintExecutor(
                jlintExecutable, classFiles);
        executor.execute(mResultStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                jlintExecutable.getAbsolutePath(),
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
        File jlintExecutable = new File(".");
        File sourceDirectory = new File("d");
        List<File> classFiles = Collections.singletonList(new File("a"));

        JlintExecutor executor = createJlintExecutor(
                jlintExecutable, classFiles);
        executor.setSourceDirectory(sourceDirectory);
        executor.execute(mResultStringBuilder);

        List<String> recordedCommand = mCommandExecutor.getRecordedCommand();
        assertEquals(
                jlintExecutable.getAbsolutePath(), recordedCommand.get(0));
        assertEquals("-source", recordedCommand.get(1));
        assertEquals(
                sourceDirectory.getAbsolutePath(), recordedCommand.get(2));
        assertEquals(
                classFiles.get(0).getAbsolutePath(), recordedCommand.get(3));
    }
}
